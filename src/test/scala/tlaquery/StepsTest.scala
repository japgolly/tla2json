package tlaquery

import sourcecode.Line
import tlaquery.TestUtil._
import utest._

object StepsTest extends TestSuite {

  private def spotCheckTestData(td: TestData, stepNo: Int)
                               (expect: String)
                               (implicit l: Line): Unit = {
    val p = "/\\ "
    val e = expect.split("\n+/\\\\").filter(_.nonEmpty).map(p + _.stripPrefix(p).trim).sorted
    val a = td.fullSteps.no(stepNo).state.variables.iterator.map { case (k, v) => s"$p$k = $v" }.toArray.sorted
    assertMultiline(a.mkString("\n"), e.mkString("\n"))
  }

  override def tests = Tests {

    "td1_1" - spotCheckTestData(TestData1, 1)(
      """/\ browsers = (b1 :> << >>)
        |/\ network = <<>>
        |/\ tabs = (t1 :> [status |-> "-"] @@ t2 :> [status |-> "-"])
        |/\ remote = {}
        |/\ workers = (w1 :> [status |-> "-"] @@ w2 :> [status |-> "-"])
        |""".stripMargin
    )

    "td1_2" - spotCheckTestData(TestData1, 2)(
      """/\ tabs = ( t1 :> [status |-> "-"] @@
        |  t2 :>
        |      [ drafts |-> {},
        |        worker |-> w1,
        |        status |-> "loading",
        |        awaiting |-> {"Remote"} ] )
        |/\ workers = ( w1 :>
        |      [ drafts |-> {},
        |        time |-> 1,
        |        status |-> "live",
        |        browser |-> b1,
        |        sync |-> [Remote |-> [desired |-> 0, lastReq |-> 0, lastAck |-> 0]] ] @@
        |  w2 :> [status |-> "-"] )
        |
        |/\ browsers = (b1 :> << >>)
        |/\ network = <<>>
        |/\ remote = {}
        |""".stripMargin
    )

    "td1_6" - spotCheckTestData(TestData1, 6)(
      """/\ tabs = ( t1 :> [drafts |-> {}, worker |-> w2, status |-> "loading", awaiting |-> {}] @@
        |  t2 :> [worker |-> w1, status |-> "clean"] )
        |
        |/\ workers = ( w1 :>
        |      [ drafts |-> {},
        |        time |-> 1,
        |        status |-> "live",
        |        browser |-> b1,
        |        sync |-> [Remote |-> [desired |-> 0, lastReq |-> 0, lastAck |-> 0]] ] @@
        |  w2 :>
        |      [ drafts |-> {},
        |        time |-> 1,
        |        status |-> "live",
        |        browser |-> b1,
        |        sync |-> [Remote |-> [desired |-> 0, lastReq |-> 0, lastAck |-> 0]] ] )
        |
        |/\ browsers = (b1 :> << >>)
        |/\ network = <<>>
        |/\ remote = {}
        |""".stripMargin
    )

  }
}
