package tlaquery

import fastparse._
import japgolly.microlibs.stdlib_ext.StdlibExt._

object Parsers {

  def preprocess(text: String): String =
    text
      .removeAnsiEscapeCodes
      .replace("\r\n", "\n")
      .trim

  object Shared {
    import fastparse.NoWhitespace._

    def EOL[_: P]: P[Unit] =
      P("\n" | End)

    def line[_: P]: P[String] =
      P(!End ~ CharPred(_ != '\n').rep.! ~ EOL)

    def lineNE[_: P]: P[String] =
      line.filter(_.nonEmpty)

    def number[_: P]: P[Int] =
      P(CharIn("0-9").rep(1).!.map(_.toInt))

    def ident[_: P]: P[String] =
      P((CharIn("0-9_").rep ~ CharIn("A-Za-z") ~ CharIn("A-Za-z0-9_").rep).!)

    def hexDigit[_: P] =
      P(CharIn("0-9a-fA-F"))
  }

  // ===================================================================================================================
  object Steps {
    import fastparse.NoWhitespace._
    import Shared._

    val preSteps = "(?s)^(.*?\n)?(?=State 1:)".r

    def descInitial[_: P]: P[Step.Desc] =
      P("Initial predicate").map(_ => Step.Desc.Initial)

    def descAction[_: P]: P[Step.Desc] =
      P(ident.! ~ " line " ~ CharPred(_ != '>').rep).map(Step.Desc.Action)

    def desc[_: P]: P[Step.Desc] =
      P(descInitial | descAction)

    def state[_: P]: P[String] =
      lineNE.rep.map(_.mkString("\n"))

    def step[_: P]: P[Step[String]] =
      P("State " ~ number ~ ": <" ~/ desc ~ ">\n" ~/ state)
        .map { case (n, d, s) => Step(n, d, s) }

    def steps[_: P]: P[Vector[Step[String]]] =
      P(step.rep(sep = "\n").map(_.toVector) ~/ ("\n" | End))
  }

  // ===================================================================================================================
  object Values {
    import fastparse.MultiLineWhitespace._
    import Shared._
    import Value._

    def precedence[A](highest: => P[A], toLowest: ((=> P[A], P[_]) => P[A])*)(implicit ctx: P[_]): P[A] = {
      def self: P[A] = precedence(highest, toLowest: _*)
      def par1: P[A] = P(("(" ~/ self ~ ")") | highest)
      toLowest.foldLeft(() => par1)((p, f) => () => P(f(p(), ctx)))()
    }

    def infixOp[_: P, A, O](expr: => P[A], op: => P[O])(f: (A, O, A) => A): P[A] =
      P(expr ~ (op ~ expr).rep).map {
        case (head, tail) => tail.foldLeft(head) { case (l, (o, r)) => f(l, o, r) }
      }

    def infixOpU[_: P, A](value: => P[A], op: => P[Unit])(f: (A, A) => A): P[A] =
      infixOp(value, op)((l, _, r) => f(l, r))

    def bool[_: P]: P[Value] =
      P("TRUE".!.map(_ => True) | "FALSE".!.map(_ => False))

    def nat[_: P]: P[Value] =
      P(CharIn("0-9").rep(1).!.map(s => Nat(s.toInt)))

    // def unicodeEscape[_: P] =
    //   P("\\u" ~~ hexDigit ~~ hexDigit ~~ hexDigit ~~ hexDigit)

    def basicEscapes[_: P]: P[String] =
      P("\\" ~~ (
        "b".!.map(_ => "\b")
          | "f".!.map(_ => "\f")
          | "n".!.map(_ => "\n")
          | "r".!.map(_ => "\r")
          | "t".!.map(_ => "\t")
          | (!End ~~ AnyChar.!)
        ))

    def substrLiteral[_: P]: P[String] =
      P(CharsWhile(c => c != '\"' && c != '\\').!)

    def str[_: P]: P[Value] =
      P("\"" ~~/ (substrLiteral | basicEscapes).repX ~~ "\"").map(fs => Str(fs.mkString))

    def modelValue[_: P]: P[Value] =
      ident.map(ModelValue)

    def seq[_: P]: P[Value] =
      P("<<" ~ value.rep(sep = ",") ~ ">>").map(vs => Seq(vs.toVector))

    def set[_: P]: P[Value] =
      P("{" ~ value.rep(sep = ",") ~ "}").map(vs => Set(vs.toVector))

    def rec[_: P]: P[Value] =
      P("[" ~ (ident ~ "|->" ~ value).rep(sep = ",") ~ "]").map(kvs => Rec(kvs.toVector))

    def :>[_: P](v: => P[Value]): P[Value] =
      infixOpU(v, P(":>"))(Value.:>)

    def @@[_: P](v: => P[Value]): P[Value] =
      infixOpU(v, P("@@"))(Value.@@)

    def value[_: P]: P[Value] =
      precedence[Value](
        bool | nat | str | modelValue | seq | set | rec,
        :>(_)(_),
        @@(_)(_),
      )

    def main[_: P]: P[Value] =
      P(value ~ End)
  }

}
