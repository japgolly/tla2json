package tlaquery

import tlaquery.Step.Desc
import tlaquery.TestUtil._
import utest._

abstract class TestData {

  val output: String

  private final val _steps: () => Vector[Step[String]] =
    timeLimitedLazy(Step.parseMany(output))

  private final val _trace: () => Step.Trace =
    timeLimitedLazy(_steps().map(_.parseState))

  private final val _traceJson =
    timeLimitedLazy(_trace().map(_.map(_.map(_.toJson))))

  final def steps = _steps()
  final def trace = _trace()
  final def traceJson = _traceJson()
}

object TestData {

  abstract class PropTest(testData: TestData) extends TestSuite {
    import testData._

    final override def tests = Tests {

      "totalSteps" - {
        val r = "^State \\d+:.*".r.pattern
        val e = output.linesIterator.count(r.matcher(_).find())
        assertEq(steps.length, e)
      }

      "eachStep" - {
        for (i <- steps.indices) {
          val s = steps(i)
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
          steps2.iterator.map { s =>
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
            .slice(output.indexOf("\nState 1:"), output.indexOf("\nThe number of states generated:"))
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