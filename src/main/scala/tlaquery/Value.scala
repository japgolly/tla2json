package tlaquery

sealed trait Value {
  final def :>(that: Value) = Value.:>(this, that)
  final def @@(that: Value) = Value.@@(this, that)
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

  final case class :>(key: Value, value: Value) extends Value

  final case class @@(key: Value, value: Value) extends Value

  val True = Bool(true)
  val False = Bool(false)

  def parse(s: String): Value =
    fastparse.parse(
      Parsers.preprocess(s),
      Parsers.Values.main(_)
    ).get.value
}