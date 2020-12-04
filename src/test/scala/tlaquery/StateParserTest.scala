package tlaquery

import utest._
import TestUtil._
import scala.collection.immutable.VectorMap
import sourcecode.Line

object StateParserTest extends TestSuite {

  private def assertParseOk(text: String, expect: State[String])(implicit l: Line): Unit = {
    val actual = timeLimited(State.parse(text))
    assertEq(actual, expect)
  }

  override def tests = Tests {

    "soleSingleLine" - {
      val actual = """browsers = (b1 :> << >>)"""
      val expect = State(VectorMap("browsers" -> """(b1 :> << >>)"""))
      assertParseOk(actual, expect)
    }

    "oneSingleLine" - {
      val actual = """/\ browsers = (b1 :> << >>)"""
      val expect = State(VectorMap("browsers" -> """(b1 :> << >>)"""))
      assertParseOk(actual, expect)
    }

    "manySingleLines" - {
      val actual =
        """
          |/\ browsers = (b1 :> << >>)
          |/\ network = <<>>
          |/\ tabs = (t1 :> [status |-> "-"] @@ t2 :> [status |-> "-"])
          |/\ remote = {}
          |/\ workers = (w1 :> [status |-> "-"] @@ w2 :> [status |-> "-"])
          |""".stripMargin
      val expect = State(VectorMap(
          "browsers" -> """(b1 :> << >>)""",
          "network" -> """<<>>""",
          "tabs" -> """(t1 :> [status |-> "-"] @@ t2 :> [status |-> "-"])""",
          "remote" -> """{}""",
          "workers" -> """(w1 :> [status |-> "-"] @@ w2 :> [status |-> "-"])""",
      ))
      assertParseOk(actual, expect)
    }

    "oneMultiLine" - {
      val actual =
        """
          |/\ network = << [type |-> "ack:R->T", from |-> "Remote", to |-> t1, id |-> 2],
          |   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
          |     type |-> "RemoteStoreCmd",
          |     from |-> w1,
          |     to |-> t2,
          |     id |-> 2 ] >>
          |""".stripMargin
      val expect = State(VectorMap(
          "network" -> actual.trim.dropWhile(_ != '<'),
      ))
      assertParseOk(actual, expect)
    }

    "manyMultiLines" - {
      val actual =
        """
          |/\ tabs = ( t1 :> [status |-> "-"] @@
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
          |/\ xxxx_123 = ( t1 :> [drafts |-> {}, worker |-> w2, status |-> "loading", awaiting |-> {}] @@
          |  t2 :> [drafts |-> {}, worker |-> w1, status |-> "loading", awaiting |-> {}] )
          |""".stripMargin
      val expect = State(VectorMap(
        "tabs" ->
          """( t1 :> [status |-> "-"] @@
            |  t2 :>
            |      [ drafts |-> {},
            |        worker |-> w1,
            |        status |-> "loading",
            |        awaiting |-> {"Remote"} ] )
            |""".stripMargin.trim,
        "workers" ->
          """( w1 :>
            |      [ drafts |-> {},
            |        time |-> 1,
            |        status |-> "live",
            |        browser |-> b1,
            |        sync |-> [Remote |-> [desired |-> 0, lastReq |-> 0, lastAck |-> 0]] ] @@
            |  w2 :> [status |-> "-"] )
            |""".stripMargin.trim,
        "xxxx_123" ->
          """( t1 :> [drafts |-> {}, worker |-> w2, status |-> "loading", awaiting |-> {}] @@
            |  t2 :> [drafts |-> {}, worker |-> w1, status |-> "loading", awaiting |-> {}] )
            |""".stripMargin.trim,
      ))
      assertParseOk(actual, expect)
    }

  }
}
