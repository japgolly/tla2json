package tlaquery

import utest._
import TestUtil._

object StepParserTest extends TestSuite {

  override def tests = Tests {

    "steps" - {
      val steps = timeLimited(Step.parse(TestData1.output))

      "len" - assertEq(steps.length, 43)

      "props" - {
        for (i <- steps.indices) {
          val s = steps(i)
          assertEq(s.desc == Step.Desc.Initial, i == 0)
          assertEq(s.no, i + 1)
        }
      }

      "actionNames" - {
        assertEq(steps(2).desc, Step.Desc.Action("TabNew"))
        assertEq(steps(21).desc, Step.Desc.Action("TabRecvDraftsFromWorker"))
        assertEq(steps(42).desc, Step.Desc.Action("WorkerRecvRemoteAck"))
      }

      "state1" -
        assertEq(steps(0).state,
          """/\ browsers = (b1 :> << >>)
            |/\ network = <<>>
            |/\ tabs = (t1 :> [status |-> "-"] @@ t2 :> [status |-> "-"])
            |/\ remote = {}
            |/\ workers = (w1 :> [status |-> "-"] @@ w2 :> [status |-> "-"])
            |""".stripMargin.trim
        )

      "state7" -
        assertEq(steps(6).state,
          """/\ tabs = ( t1 :> [worker |-> w2, status |-> "clean"] @@
            |  t2 :> [worker |-> w1, status |-> "clean"] )
            |""".stripMargin.trim
        )

      "state43" -
        assertEq(steps(42).state,
          """/\ network = <<>>
            |/\ workers = ( w1 :>
            |      [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
            |        time |-> 2,
            |        status |-> "live",
            |        browser |-> b1,
            |        sync |-> [Remote |-> [desired |-> 2, lastReq |-> 2, lastAck |-> 2]] ] @@
            |  w2 :>
            |      [ drafts |->
            |            { [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)],
            |              [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] },
            |        time |-> 2,
            |        status |-> "live",
            |        browser |-> b1,
            |        sync |-> [Remote |-> [desired |-> 2, lastReq |-> 2, lastAck |-> 2]] ] )
            |""".stripMargin.trim
        )
    }

  }
}
