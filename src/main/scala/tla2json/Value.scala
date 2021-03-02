package tla2json

import io.circe._
import japgolly.microlibs.stdlib_ext.StdlibExt._
import scala.collection.immutable.ArraySeq

sealed abstract class Value(final val descType: String) {
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

  final def asNat: Either[String, Nat] =
    this match {
      case n: Nat => Right(n)
      case _      => Left("Expected a Nat, got a " + descType)
    }

  final def asStr: Either[String, Str] =
    this match {
      case s: Str => Right(s)
      case _      => Left("Expected a string, got a " + descType)
    }

  final def asRecord: Either[String, Rec] =
    this match {
      case r: Rec => Right(r)
      case _      => Left("Expected a record, got a " + descType)
    }
}

object Value {

  final case class Bool(value: Boolean) extends Value("boolean")

  final case class Nat(value: Long) extends Value("Nat")

  final case class Str(value: String) extends Value("string")

  final case class ModelValue(value: String) extends Value("model value")

  /** <<...>> */
  final case class Seq(value: ArraySeq[Value]) extends Value("sequence")

  /** {...} */
  final case class Set(value: ArraySeq[Value]) extends Value("set")

  /** [kₙ |-> vₙ] */
  final case class Rec(value: ArraySeq[(String, Value)]) extends Value("record") {
    def apply(k: String): Either[String, Value] =
      value.find(_._1 == k) match {
        case Some((_, v)) => Right(v)
        case None         => Left(s"'$k' key not found")
      }

    def filterKeys(f: String => Boolean): Rec =
      Rec(value.filter(x => f(x._1)))

    def -(keyToRemove: String) =
      filterKeys(_ != keyToRemove)
  }

  final case class :>(lhs: Value, rhs: Value) extends Value("(X :> Y)")

  final case class @@(lhs: Value, rhs: Value) extends Value("(X @@ Y)") {
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