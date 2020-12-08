package tlaquery

import io.circe._
import japgolly.microlibs.stdlib_ext.StdlibExt._

sealed trait Value {
  import Value._

  final def :>(that: Value) = Value.:>(this, that)
  final def @@(that: Value) = Value.@@(this, that)

  final def asJsonObjectKey: Option[String] =
    this match {
      case Str(a)        => Some(a)
      case ModelValue(a) => Some(a)
      case Nat(a)        => Some(a.toString)
      case Bool(a)       => Some(a.toString)
      case _             => None
    }

  /** Note: The point of this isn't to be a canonical representation. The point is to have something easily readable
    * and queryable by humans. This assumes that certain ambiguities (eg. {} vs <<>>) are acceptable because the human
    * reader understands the domain and thus in unambiguous in that lens.
    */
  final def toJson: Json = {
    this match {
      case Bool(v)       => Json.fromBoolean(v)
      case Nat(v)        => Json.fromLong(v)
      case Str(v)        => Json.fromString(v)
      case ModelValue(v) => Json.fromString(v)
      case Seq(vs)       => Json.arr(vs.map(_.toJson): _*)
      case Set(vs)       => Json.arr(vs.map(_.toJson): _*)
      case Rec(kvs)      => Json.obj(kvs.map(_.map2(_.toJson)): _*)
      case x: @@ =>
        x.toRecord match {
          case Some(kvs) => Json.obj(kvs.map(_.map2(_.toJson)): _*)
          case None      => Json.arr(x.lhs.toJson, x.rhs.toJson)
        }
      case :>(k ,v) =>
        k.asJsonObjectKey match {
          case Some(k2) => Json.obj(k2 -> v.toJson)
          case None     => Json.arr(k.toJson, v.toJson)
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

      def scan(value: Value): Boolean =
        value match {
          case x: @@ => scan(x.lhs) && scan(x.rhs)
          case :>(k, v) =>
            k.asJsonObjectKey match {
              case Some(k) => add(k, v)
              case None    => false
            }
          case _ => false
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