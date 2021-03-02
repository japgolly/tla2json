package tla2json

import scala.collection.immutable.ArraySeq
import tla2json.Step.Desc
import tla2json.TestUtil._
import utest._

abstract class TestData {

  val output: String

  private final val _steps: () => Steps.Parsed =
    timeLimitedLazy(Steps.parse(output))

  private final val _trace: () => Steps.Trace =
    timeLimitedLazy(Steps.parseTrace(_steps()))

  private final val _traceJson =
    timeLimitedLazy(_trace().withJsonValues)

  private final val _fullTraceJson =
    timeLimitedLazy(_traceJson().withFullStatePerStep)

  private final val _fullTrace =
    timeLimitedLazy(_trace().withFullStatePerStep)

  private final val _fullSteps =
    timeLimitedLazy(
      _steps()
        .fold(_.mapSteps(State.fromRecord(_).map(_.toJson.spaces2SortKeys)), _.mapSteps(State.parse))
        .withFullStatePerStep)

  final def steps         = _steps()
  final def trace         = _trace()
  final def traceJson     = _traceJson()
  final def fullTraceJson = _fullTraceJson()
  final def fullTrace     = _fullTrace()
  final def fullSteps     = _fullSteps()
}

object TestData {

  abstract class PropTest extends TestSuite {

    val testData: TestData
    def firstPostStateLine = ""

    import testData._

    def traceLines: String = {
      val start = output.indexOf("\nState 1:").max(0)
      val end = if (firstPostStateLine.isEmpty) output.length - 1 else output.indexOf("\n" + firstPostStateLine)
      output.slice(start, end)
    }

    final override def tests = Tests {

      "totalSteps" - {
        val r = "(?:^State \\d+:.*)|(?:\\b_TEAction\\b)".r.pattern
        val e = output.linesIterator.count(r.matcher(_).find())
        assertEq(trace.length, e)
      }

      "eachStep" - {
        val ss = trace.values
        for (i <- ss.indices) {
          val s = ss(i)
          assertEq(s.desc == Step.Desc.Initial, i == 0)
          assertEq(s.no, i + 1)
        }
      }

      "trace" - {
        // Evaluation is the test because I decided to make the parse methods unsafe.
        // No exception thrown == test success.
        trace
        ()
      }

      "reconstruction" - {
        steps match {

          case Right(strSteps) =>

            val steps2 =
              strSteps.map(_.map { s1 =>
                val s2 = State.parse(s1)
                val s3 = s2.variables.iterator.map { case (k, v) => s"/\\ $k = $v" }.mkString("\n")
                s3
              })

            val actual =
              steps2.values.iterator.map { s =>
                val desc = s.desc match {
                  case Desc.Initial      => "<Initial>"
                  case Desc.Action(name) => s"<$name>"
                  case Desc.Stuttering   => "Stuttering"
                }
                s"""State ${s.no}: $desc
                   |${s.state}
                   |""".stripMargin.trim
              }.mkString("\n\n")

            val expect =
              traceLines
                .replaceAll("(?m)^(State \\d+: <.+?) .*>$", "$1>")
                .trim

            //        println("#"*120)
            //        println(expect)
            //        println("#"*120)

            if (actual.trim.isEmpty)
              fail("Parser returned an empty result.")
            else
              assertMultiline(actual, expect)

          case Left(valSteps) =>

            val actualRecs =
              valSteps.values.map { step =>
                val tea =
                  step.desc match {
                    case d@(Desc.Initial | Desc.Stuttering) =>
                      Value.Rec(ArraySeq(
                        "position" -> Value.Nat(step.no),
                        "name" -> Value.Str(d.name),
                        "location" -> Value.Str("Unknown location"),
                      ))
                    case Desc.Action(desc) =>
                      val split = "^(.+?)(?: (.+))?$".r
                      val split(name, loc) = desc
                      Value.Rec(ArraySeq(
                        "position" -> Value.Nat(step.no),
                        "name" -> Value.Str(name),
                        "location" -> Value.Str(loc),
                      ))
                  }
                Value.Rec(("_TEAction", tea) +: step.state.value)
              }

            if (actualRecs.isEmpty)
              fail("Parser returned an empty result.")
            else {
              val actual = Value.Seq(actualRecs).toJson
              val expect = Value.parse(output).toJson
              assertJson(actual, expect)
            }
        }
      }

    }
  }

}