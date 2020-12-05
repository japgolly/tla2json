package tlaquery

import io.circe._
import japgolly.microlibs.stdlib_ext.StdlibExt._

sealed trait Value {

  final def :>(that: Value) = Value.:>(this, that)
  final def @@(that: Value) = Value.@@(this, that)

  /** Note: The point of this isn't to be a canonical representation. The point is to have something easily readable
    * and queryable by humans. This assumes that certain ambiguities (eg. {} vs <<>>) are acceptable because the human
    * reader understands the domain and thus in unambiguous in that lens.
    */
  final def toJson: Json = {
    import Value._
    this match {
      case Bool(v)       => Json.fromBoolean(v)
      case Nat(v)        => Json.fromLong(v)
      case Str(v)        => Json.fromString(v)
      case ModelValue(v) => Json.fromString(v)
      case Seq(vs)       => Json.arr(vs.map(_.toJson): _*)
      case Set(vs)       => Json.arr(vs.map(_.toJson): _*)
      case Rec(kvs)      => Json.obj(kvs.map(_.map2(_.toJson)): _*)
      case :>(k, v)      => Json.arr(k.toJson, v.toJson)
      case x: @@ =>
        x.toRecord match {
          case Some(kvs) => Json.obj(kvs.map(_.map2(_.toJson)): _*)
          case None      => Json.arr(x.lhs.toJson, x.rhs.toJson)
        }
    }
  }
}

object Value {

  final case class Bool(value: Boolean) extends Value

  final case class Nat(value: Long) extends Value

  final case class Str(value: String) extends Value

  final case class ModelValue(value: String) extends Value

  /** <<...>> */
  final case class Seq(value: Vector[Value]) extends Value

  /** {...} */
  final case class Set(value: Vector[Value]) extends Value

  /** [kₙ |-> vₙ] */
  final case class Rec(value: Vector[(String, Value)]) extends Value

  final case class :>(lhs: Value, rhs: Value) extends Value

  final case class @@(lhs: Value, rhs: Value) extends Value {
    def toRecord: Option[List[(String, Value)]] = {
      var found = List.empty[(String, Value)]

      @inline def add(k: String, v: Value) = {
        found ::= ((k, v)); true
      }

      def scan(v: Value): Boolean =
        v match {
          case x: @@                => scan(x.lhs) && scan(x.rhs)
          case :>(Str(k), v)        => add(k, v)
          case :>(ModelValue(k), v) => add(k, v)
          case _                    => false
        }

      Option.when(scan(this))(found)
    }
  }

  val True = Bool(true)
  val False = Bool(false)

  def parse(s: String): Value =
    fastparse.parse(
      Parsers.preprocess(s),
      Parsers.Values.main(_)
    ).get.value
}