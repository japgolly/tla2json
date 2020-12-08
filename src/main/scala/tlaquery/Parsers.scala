package tlaquery

import fastparse._
import japgolly.microlibs.stdlib_ext.StdlibExt._
import scala.annotation._
import scala.collection.immutable.VectorMap

object Parsers {

  def preprocess(text: String): String =
    text
      .removeAnsiEscapeCodes
      .replace("\r\n", "\n")
      .trim

  object Shared {
    import fastparse.NoWhitespace._

    def dropRightWhile(s: String, drop: Char => Boolean): String = {
      var i = s.length
      var n = 0
      while (i > 0) {
        i -= 1
        val c = s(i)
        if (drop(c))
          n += 1
        else
          i = 0
      }
      if (n > 0)
        s.dropRight(n)
      else
        s
    }

    // Faster version of Character.isWhitespace
    // Generated with:
    // (1 to 65535).filter(i => i!=32 && Character.isWhitespace(i.toChar)).map("'\\u%04x'".format(_)).mkString("case ' ' | ", " | ", " => true")
    def isWhitespace(c: Char): Boolean =
      (c: @switch) match {
        case ' '
             | '\u0009' | '\u000a' | '\u000b' | '\u000c' | '\u000d' | '\u001c' | '\u001d' | '\u001e' | '\u001f'
             | '\u1680' | '\u180e' | '\u2000' | '\u2001' | '\u2002' | '\u2003' | '\u2004' | '\u2005' | '\u2006'
             | '\u2008' | '\u2009' | '\u200a' | '\u2028' | '\u2029' | '\u205f' | '\u3000' =>
          true
        case _ =>
          false
      }

    def WS[_: P]: P[Unit] =
      P(CharsWhile(isWhitespace, 1))

    def OWS[_: P]: P[Unit] =
      P(CharsWhile(isWhitespace, 0))

    def EOL[_: P]: P[Unit] =
      P("\n" | End)

    def line[_: P]: P[String] =
      P(!End ~ CharsWhile(_ != '\n', 0).! ~ EOL)

    def lineNE[_: P]: P[String] =
      line.filter(_.nonEmpty)

    def blankLine[_: P]: P[Unit] =
      P(!End ~ OWS ~ EOL)

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
    import Step.Desc

    val preSteps = "(?s)^(.*?\n)?(?=State 1:)".r

    def descInitial[_: P]: P[Desc] =
      P("<Initial predicate>").map(_ => Desc.Initial)

    def descAction[_: P]: P[Desc] =
      P("<" ~ ident.! ~ " line " ~ CharsWhile(_ != '>') ~ ">").map(Desc.Action)

    def descStuttering[_: P]: P[Desc] =
      P("Stuttering").map(_ => Desc.Stuttering)

    def desc[_: P]: P[Desc] =
      P(descInitial | descAction | descStuttering)

    def state[_: P]: P[String] =
      lineNE.rep.map(_.mkString("\n"))

    def stepDecl[_: P]: P[Int] =
      P("State " ~ number ~ ": ")

    def step[_: P]: P[Step[String]] =
      P(stepDecl  ~/ desc).flatMap {
        case (n, d@ Desc.Stuttering) => Pass.map(_ => Step(n, d, ""))
        case (n, d)                  => P("\n" ~/ state).map(Step(n, d, _))
      }

    def stepSep[_: P]: P[Unit] =
      P("\n" ~ (!stepDecl ~ line).rep(0))

    def steps[_: P]: P[Steps[String]] =
      P(step.rep(sep = stepSep)).map(ss => tlaquery.Steps(ss.toVector))

    def main[_: P]: P[Steps[String]] =
      P(steps) // Deliberately not adding ` ~ End` here because we want to ignore  the tail.
  }

  // ===================================================================================================================
  object States {
    import fastparse.NoWhitespace._
    import Shared._

    def variable[_: P]: P[(String, String)] =
      P(("""/\""" ~ WS).? ~ ident ~ WS ~ "=" ~ WS ~ body)

    def body[_: P]: P[String] =
      P((line ~ (" " ~ lineNE.filter(!_.forall(isWhitespace))).rep).!)
        .map(dropRightWhile(_, isWhitespace))

    def state[_: P]: P[State[String]] =
      P(variable.rep(sep = blankLine.rep)).map(kvs => State(kvs.to(VectorMap)))

    def main[_: P]: P[State[String]] =
      P(state ~ End)
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
