package tlaquery

import tlaquery.Step.Desc
import tlaquery.TestUtil._
import utest._

abstract class TestData {

  val output: String

  private final val _steps: () => Steps[String] =
    timeLimitedLazy(Steps.parse(output))

  private final val _trace: () => Steps.Trace =
    timeLimitedLazy(_steps().map(_.parseState))

  private final val _traceJson =
    timeLimitedLazy(_trace().withJsonValues)

  private final val _fullTraceJson =
    timeLimitedLazy(_traceJson().withFullStatePerStep)

  private final val _fullTrace =
    timeLimitedLazy(_trace().withFullStatePerStep)

  private final val _fullSteps =
    timeLimitedLazy(_steps().mapSteps(State.parse).withFullStatePerStep)

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
    def firstPostStateLine: String

    final override def tests = Tests {
      import testData._

      "totalSteps" - {
        val r = "^State \\d+:.*".r.pattern
        val e = output.linesIterator.count(r.matcher(_).find())
        assertEq(steps.length, e)
      }

      "eachStep" - {
        val ss = steps.values
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
        val steps2 =
          steps.map(_.map { s1 =>
            val s2 = State.parse(s1)
            val s3 = s2.variables.iterator.map { case (k, v) => s"/\\ $k = $v" }.mkString("\n")
            s3
          })

        val actual =
          steps2.values.iterator.map { s =>
            val desc = s.desc match {
              case Desc.Initial      => "Initial"
              case Desc.Action(name) => name
            }
            s"""State ${s.no}: <$desc>
               |${s.state}
               |""".stripMargin.trim
          }.mkString("\n\n")

        val expect =
          output
            .slice(output.indexOf("\nState 1:"), output.indexOf("\n" + firstPostStateLine))
            .replaceAll("(?m)^(State \\d+: <.+?) .*>$", "$1>")
            .trim

//        println("#"*120)
//        println(expect)
//        println("#"*120)

        assertMultiline(actual, expect)
      }
    }
  }

}