package tlaquery

import utest._
import TestUtil._
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
      "{}" - assertParseOk(Set(Vector.empty))
      "{{}}" - assertParseOk(Set(Vector(Set(Vector.empty))))
      "{1,2,3}" - assertParseOk(Set(Vector(Nat(1), Nat(2), Nat(3))))
    }

    "seq" - {
      "<<>>" - assertParseOk(Seq(Vector.empty))
      "<< >>" - assertParseOk(Seq(Vector.empty))
      "<< << >> >>" - assertParseOk(Seq(Vector(Seq(Vector.empty))))
      "<<<<>>>>" - assertParseOk(Seq(Vector(Seq(Vector.empty))))
      "<<1,2,3>>" - assertParseOk(Seq(Vector(Nat(1), Nat(2), Nat(3))))
    }

    "rec" - {
      "[]" - assertParseOk(Rec(Vector.empty))
      "[blah_2 |-> 2]" - assertParseOk(Rec(Vector("blah_2" -> Nat(2))))
      "[a |-> [b|->8], c |-> 3]" - assertParseOk(Rec(Vector("a" -> Rec(Vector("b" -> Nat(8))), "c" -> Nat(3))))
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
        val t1 = ModelValue("t1") :> Rec(Vector(
          "drafts" -> Set(Vector.empty),
          "worker" -> ModelValue("w2"),
          "status" -> Str("loading"),
          "awaiting" -> Set(Vector.empty),
        ))
        val t2 = ModelValue("t2") :> Rec(Vector(
          "worker" -> ModelValue("w1"),
          "status" -> Str("clean"),
        ))
        val expect = t1 @@ t2
        assertParseOk(txt, expect)
      }
    }
  }
}
