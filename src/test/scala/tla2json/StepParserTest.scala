package tla2json

import utest._
import TestUtil._

object StepParserTest extends TestSuite {

  override def tests = Tests {

    "steps" - {
      val steps = TestData1.steps.getOrThrow()

      "len" - assertEq(steps.length, 43)

      "actionNames" - {
        assertEq(steps.no(3).desc, Step.Desc.Action("TabNew"))
        assertEq(steps.no(22).desc, Step.Desc.Action("TabRecvDraftsFromWorker"))
        assertEq(steps.no(43).desc, Step.Desc.Action("WorkerRecvRemoteAck"))
      }

      "state1" -
        assertEq(steps.no(1).state,
          """/\ browsers = (b1 :> << >>)
            |/\ network = <<>>
            |/\ tabs = (t1 :> [status |-> "-"] @@ t2 :> [status |-> "-"])
            |/\ remote = {}
            |/\ workers = (w1 :> [status |-> "-"] @@ w2 :> [status |-> "-"])
            |""".stripMargin.trim
        )

      "state7" -
        assertEq(steps.no(7).state,
          """/\ tabs = ( t1 :> [worker |-> w2, status |-> "clean"] @@
            |  t2 :> [worker |-> w1, status |-> "clean"] )
            |""".stripMargin.trim
        )

      "state43" -
        assertEq(steps.no(43).state,
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
