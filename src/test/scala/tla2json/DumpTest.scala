package tla2json

import scala.collection.immutable.ArraySeq
import sourcecode.Line
import tla2json.TestUtil._
import utest._

object DumpTest extends TestSuite {

  private def assertStateByState[A: UnivEq](actual: Dump[A], expect: Dump[A])(implicit l: Line): Unit = {
    assertEq("State count", actual.length, expect.length)
    for (i <- expect.states.indices) {
      val a = actual.states(i)
      val e = expect.states(i)
      assertEq(a, e)
    }
  }

  override def tests = Tests {

    "1" - {
      def test(suffix: String) = {
        val input =
          """State 1:
            |/\ target = [drafts |-> {}, pending |-> {}, returning |-> {}]
            |/\ workers = (w1 :> [status |-> "-"] @@ w2 :> [status |-> "-"])
            |/\ tabs = (t1 :> [status |-> "-"] @@ t2 :> [status |-> "-"])
            |/\ remote = [drafts |-> {}, ord |-> 0]
            |/\ browsers = (b1 :> << >> @@ b2 :> << >>)
            |/\ network = <<>>
            |
            |State 2:
            |/\ target = [drafts |-> {}, pending |-> {}, returning |-> {}]
            |/\ workers = ( w1 :>
            |      [ drafts |-> {},
            |        time |-> 1,
            |        status |-> "live",
            |        sync |->
            |            [Remote |-> [tombstones |-> {}, sync |-> FALSE, syncing |-> FALSE]],
            |        browser |-> b1 ] @@
            |  w2 :> [status |-> "-"] )
            |/\ tabs = ( t1 :>
            |      [ drafts |-> {},
            |        worker |-> w1,
            |        status |-> "loading",
            |        awaiting |-> {"Remote"} ] @@
            |  t2 :> [status |-> "-"] )
            |/\ remote = [drafts |-> {}, ord |-> 0]
            |/\ browsers = (b1 :> << >> @@ b2 :> << >>)
            |/\ network = <<>>
            |""".stripMargin.trim
        val actual = Dump.parse(input + suffix)
        val expect = Dump(ArraySeq(
          State.parse(
            """
              |/\ target = [drafts |-> {}, pending |-> {}, returning |-> {}]
              |/\ workers = (w1 :> [status |-> "-"] @@ w2 :> [status |-> "-"])
              |/\ tabs = (t1 :> [status |-> "-"] @@ t2 :> [status |-> "-"])
              |/\ remote = [drafts |-> {}, ord |-> 0]
              |/\ browsers = (b1 :> << >> @@ b2 :> << >>)
              |/\ network = <<>>
              |""".stripMargin.trim),
          State.parse(
            """
              |/\ target = [drafts |-> {}, pending |-> {}, returning |-> {}]
              |/\ workers = ( w1 :>
              |      [ drafts |-> {},
              |        time |-> 1,
              |        status |-> "live",
              |        sync |->
              |            [Remote |-> [tombstones |-> {}, sync |-> FALSE, syncing |-> FALSE]],
              |        browser |-> b1 ] @@
              |  w2 :> [status |-> "-"] )
              |/\ tabs = ( t1 :>
              |      [ drafts |-> {},
              |        worker |-> w1,
              |        status |-> "loading",
              |        awaiting |-> {"Remote"} ] @@
              |  t2 :> [status |-> "-"] )
              |/\ remote = [drafts |-> {}, ord |-> 0]
              |/\ browsers = (b1 :> << >> @@ b2 :> << >>)
              |/\ network = <<>>
              |""".stripMargin.trim),
        ))
        assertStateByState(actual, expect)
      }
      "a" - test("")
      "b" - test("\n\n")
    }
  }
}
