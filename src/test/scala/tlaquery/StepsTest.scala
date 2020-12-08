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

    "success" - {
      val output =
        """> cd /tmp/20201207-171946-1384467 && unbuffer tlc-colour -difftrace drafts
          |TLC2 Version 2.15 of Day Month 20?? (rev: e389d55)
          |Running breadth-first search Model-Checking with fp 97 and seed 2236431244207455344 with 24 workers on 24 cores with 25486MB heap and 64MB offheap memory [pid: 1384481] (Linux 5.9.11-arch2-1 amd64, GraalVM Community 11.0.8 x86_64, MSBDiskFPSet, DiskStateQueue).
          |Parsing file /tmp/20201207-171946-1384467/drafts.tla
          |Parsing file /tmp/FiniteSets.tla
          |Parsing file /tmp/Naturals.tla
          |Parsing file /tmp/Sequences.tla
          |Parsing file /tmp/TLC.tla
          |Parsing file /tmp/20201207-171946-1384467/Util.tla
          |Semantic processing of module Naturals
          |Semantic processing of module Sequences
          |Semantic processing of module FiniteSets
          |Semantic processing of module TLC
          |Semantic processing of module Util
          |Semantic processing of module drafts
          |Starting... (2020-12-07 17:19:46)
          |Computing initial states...
          |Finished computing initial states: 1 distinct state generated at 2020-12-07 17:19:46.
          |Model checking completed. No error has been found.
          |  Estimates of the probability that TLC did not check all reachable states
          |  because two distinct states had the same fingerprint:
          |  calculated (optimistic):  val = 4.9E-18
          |23 states generated, 18 distinct states found, 0 states left on queue.
          |The depth of the complete state graph search is 13.
          |The average outdegree of the complete state graph is 1 (minimum is 0, the maximum 2 and the 95th percentile is 2).
          |Finished in 00s at (2020-12-07 17:19:46)
          |""".stripMargin

      val actual = Steps.parse(output)
      assert(actual.isEmpty)
    }
  }
}
