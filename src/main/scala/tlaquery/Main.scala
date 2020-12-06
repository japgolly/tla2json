package tlaquery

import io.circe.Json
import java.io._
import java.nio.charset.StandardCharsets
import scala.util.Using

object Main {

  private final val cliName = "tla-query"
  private final val displayName = cliName

  final case class Options(
      parseTrace: Boolean      = false,
      parseState: Boolean      = false,
      parseValue: Boolean      = false,
      fullTrace : Boolean      = false,
      indentJson: Boolean      = false,
      inputFile : Option[File] = None,
    )

  private implicit class ExtScoptOptionDef[A, C](private val self: scopt.OptionDef[A, C]) extends AnyVal {
    def action_(f: C => C) = self.action((_, c) => f(c))
  }

  private val optParser = new scopt.OptionParser[Options](cliName) {
    head(displayName, "v" + BuildInfo.version)

    opt[Unit]('t', "trace").action_(_.copy(parseTrace = true)).text("Parse input as a trace (default)")
    opt[Unit]('s', "state").action_(_.copy(parseState = true)).text("Parse input as a state")
    opt[Unit]('v', "value").action_(_.copy(parseValue = true)).text("Parse input as a value")
    opt[Unit]('f', "full").action_(_.copy(fullTrace = true)).text("Convert a diff-trace back into a full trace")
    opt[Unit]('i', "indent").action_(_.copy(indentJson = true)).text("Indent JSON output")

    arg[File]("<input file>")
      .optional()
      .action((f, c) => c.copy(inputFile = Some(f)))
      .text("File to parse")

    checkConfig { o =>
      import o._

      val result =
        if (List(parseTrace, parseState, parseValue).count(identity) > 1)
          failure("Only one parse method can be specified.")
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
      else {
        var trace = Steps.parseTrace(content).withJsonValues
        if (opts.fullTrace)
          trace = trace.withFullStatePerStep
        trace.toJson
      }

    def result: String =
      if (opts.indentJson)
        json.spaces2
      else
        json.noSpaces

    println(result)
  }
}
