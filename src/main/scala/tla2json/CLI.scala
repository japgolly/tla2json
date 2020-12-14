package tla2json

import io.circe.Json
import java.io._
import java.nio.charset.StandardCharsets
import scala.util.Using

object CLI {

  private final val cliName = "tla2json"
  private final val displayName = cliName

  final case class Options(
      parseDump   : Boolean      = false,
      parseTrace  : Boolean      = false,
      parseState  : Boolean      = false,
      parseValue  : Boolean      = false,
      toDiffTrace : Boolean      = false,
      toFullTrace : Boolean      = false,
      fullLastStep: Boolean      = false,
      indentJson  : Boolean      = false,
      sortKeys    : Boolean      = false,
      inputFile   : Option[File] = None,
    )

  private implicit class ExtScoptOptionDef[A, C](private val self: scopt.OptionDef[A, C]) extends AnyVal {
    def action_(f: C => C) = self.action((_, c) => f(c))
  }

  private val optParser = new scopt.OptionParser[Options](cliName) {
    head(displayName, "v" + BuildInfo.version)
    head()

    opt[Unit]('D', "dump"          ).action_(_.copy(parseDump    = true)).text("Parse input as a dump")
    opt[Unit]('T', "trace"         ).action_(_.copy(parseTrace   = true)).text("Parse input as a trace (default)")
    opt[Unit]('S', "state"         ).action_(_.copy(parseState   = true)).text("Parse input as a state")
    opt[Unit]('V', "value"         ).action_(_.copy(parseValue   = true)).text("Parse input as a value")
    opt[Unit]('d', "diff"          ).action_(_.copy(toDiffTrace  = true)).text("Convert a full trace into a diff-trace")
    opt[Unit]('f', "full"          ).action_(_.copy(toFullTrace  = true)).text("Convert a diff-trace back into a full trace")
    opt[Unit]('l', "full-last-step").action_(_.copy(fullLastStep = true)).text("Convert the last step of a diff-trace back into a full trace")
    opt[Unit]('i', "indent"        ).action_(_.copy(indentJson   = true)).text("Indent and pretty-print JSON output")
    opt[Unit]('s', "sort"          ).action_(_.copy(sortKeys     = true)).text("Output the fields of each object with the keys in sorted order")

    arg[File]("<input file>")
      .optional()
      .action((f, c) => c.copy(inputFile = Some(f)))
      .text("File to parse")

    checkConfig { o =>
      import o._

      val result =
        if (List(parseDump, parseTrace, parseState, parseValue).count(identity) > 1)
          failure("Only one parse method can be specified.")
        else if (toDiffTrace & toFullTrace)
          failure("Did you want a diff trace or a full trace?")
        else
          success

      result.left.map(_ + "\n")
    }
  }

  def main(args: Array[String]): Unit =
    optParser.parse(args, Options()).foreach(run)

  // ===================================================================================================================

  private def run(opts: Options): Unit = {

    // TODO: Add proper streaming support
    def content: String =
      Using.Manager { use =>

        val stream: InputStream =
          opts.inputFile match {
            case Some(f) => use(new FileInputStream(f))
            case None    => System.in
          }

        val isr = use(new InputStreamReader(stream, StandardCharsets.UTF_8))
        val br = use(new BufferedReader(isr))

        Iterator.continually(br.readLine()).takeWhile(_ != null).mkString("\n")
      }.get

    def json: Json =
      if (opts.parseValue)
        Value.parse(content).toJson
      else if (opts.parseState)
        State.parse(content).toJson(Value.parse(_).toJson)
      else if (opts.parseDump)
        Dump.parse(content).toJson(Value.parse(_).toJson)
      else {
        var trace = Steps.parseTrace(content).withJsonValues
        if (opts.toFullTrace)
          trace = trace.withFullStatePerStep
        else if (opts.toDiffTrace)
          trace = trace.withDiffStatePerStep[Json](_ == _, lastFull = opts.fullLastStep)
        else if (opts.fullLastStep && trace.nonEmpty) {
          val full = trace.withFullStatePerStep
          trace = Steps(trace.values.init :+ full.values.last)
        }
        trace.toJson
      }

    def result: String =
      (opts.indentJson, opts.sortKeys) match {
        case (false, false) => json.noSpaces
        case (false, true ) => json.noSpacesSortKeys
        case (true , false) => json.spaces2
        case (true , true ) => json.spaces2SortKeys
      }

    println(result)
  }
}
