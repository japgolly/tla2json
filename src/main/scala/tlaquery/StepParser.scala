package tlaquery

import fastparse._
import fastparse.NoWhitespace._
import japgolly.microlibs.stdlib_ext.StdlibExt._

object StepParser {
  import Internal._

  def apply(output: String): Vector[Step[String]] = {
    var content =
      output
        .removeAnsiEscapeCodes
        .replace("\r\n", "\n")

    content = preSteps.replaceFirstIn(content, "")

    parse(content, steps(_)).get.value
  }

  private object Internal {

    val preSteps = "(?s)^(.*?\n)?(?=State 1:)".r

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

}
