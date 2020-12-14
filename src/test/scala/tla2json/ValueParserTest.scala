package tla2json

import utest._
import TestUtil._
import scala.collection.immutable.ArraySeq
import sourcecode.Line
import utest.framework.TestPath

object ValueParserTest extends TestSuite {
  import Value._

  private def assertParseOk(expect: Value)(implicit l: Line, tp: TestPath): Unit =
    assertParseOk(tp.value.last, expect)

  private def assertParseOk(text: String, expect: Value)(implicit l: Line): Unit = {
    val actual = timeLimited(Value.parse(text))
    assertEq(actual, expect)
  }

  override def tests = Tests {

    "bool" - {
      "TRUE" - assertParseOk(Bool(true))
      "FALSE" - assertParseOk(Bool(false))
    }

    "nat" - {
      "0" - assertParseOk(Nat(0))
      "123" - assertParseOk(Nat(123))
    }

    "str" - {
      "\"\"" - assertParseOk(Str(""))
      "\"omg\"" - assertParseOk(Str("omg"))
      "\" \\\" \\\\ \"" - assertParseOk(Str(" \" \\ "))
    }

    "set" - {
      "{}" - assertParseOk(Set(ArraySeq.empty))
      "{{}}" - assertParseOk(Set(ArraySeq(Set(ArraySeq.empty))))
      "{1,2,3}" - assertParseOk(Set(ArraySeq(Nat(1), Nat(2), Nat(3))))
    }

    "seq" - {
      "<<>>" - assertParseOk(Seq(ArraySeq.empty))
      "<< >>" - assertParseOk(Seq(ArraySeq.empty))
      "<< << >> >>" - assertParseOk(Seq(ArraySeq(Seq(ArraySeq.empty))))
      "<<<<>>>>" - assertParseOk(Seq(ArraySeq(Seq(ArraySeq.empty))))
      "<<1,2,3>>" - assertParseOk(Seq(ArraySeq(Nat(1), Nat(2), Nat(3))))
    }

    "rec" - {
      "[]" - assertParseOk(Rec(ArraySeq.empty))
      "[blah_2 |-> 2]" - assertParseOk(Rec(ArraySeq("blah_2" -> Nat(2))))
      "[a |-> [b|->8], c |-> 3]" - assertParseOk(Rec(ArraySeq("a" -> Rec(ArraySeq("b" -> Nat(8))), "c" -> Nat(3))))
    }

    "modelValue" - {
      "x" - assertParseOk(ModelValue("x"))
      "x1" - assertParseOk(ModelValue("x1"))
      "QWE" - assertParseOk(ModelValue("QWE"))
    }

    "1 :> 2" - assertParseOk(Nat(1) :> Nat(2))
    "1 @@ 2" - assertParseOk(Nat(1) @@ Nat(2))

    "parens" - {
      "(1)" - assertParseOk(Nat(1))
      "((1))" - assertParseOk(Nat(1))
      "( ( 1 ) )" - assertParseOk(Nat(1))
      " ( ( 1 ) ) " - assertParseOk(Nat(1))
      "1 :> (2 :> 3)" - assertParseOk(Nat(1) :> (Nat(2) :> Nat(3)))
      "(1 :> 2) :> 3" - assertParseOk((Nat(1) :> Nat(2)) :> Nat(3))
    }

    "combo" - {
      "1" - {
        val txt = "w1 :> 2 @@ w2 :> 4 @@ w3 :> 6"
        val w1 = ModelValue("w1") :> Nat(2)
        val w2 = ModelValue("w2") :> Nat(4)
        val w3 = ModelValue("w3") :> Nat(6)
        assertParseOk(txt, w1 @@ w2 @@ w3)
      }

      "2" - {
        val txt =
          """( t1 :> [drafts |-> {}, worker |-> w2, status |-> "loading", awaiting |-> {}] @@
            |  t2 :> [worker |-> w1, status |-> "clean"] )
            |""".stripMargin
        val t1 = ModelValue("t1") :> Rec(ArraySeq(
          "drafts" -> Set(ArraySeq.empty),
          "worker" -> ModelValue("w2"),
          "status" -> Str("loading"),
          "awaiting" -> Set(ArraySeq.empty),
        ))
        val t2 = ModelValue("t2") :> Rec(ArraySeq(
          "worker" -> ModelValue("w1"),
          "status" -> Str("clean"),
        ))
        val expect = t1 @@ t2
        assertParseOk(txt, expect)
      }
    }
  }
}
