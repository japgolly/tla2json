package tla2json

import io.circe.Json
import scala.collection.immutable.ArraySeq

final case class Dump[A](states: ArraySeq[State[A]]) extends AnyVal {

  def length   = states.length
  def isEmpty  = states.isEmpty
  def nonEmpty = states.nonEmpty

  def map[B](f: A => B): Dump[B] =
    Dump(states.map(_.map(f)))

  def toJson(implicit f: A => Json): Json =
    Json.arr(states.map(_.toJson(f)): _*)
}

object Dump {

  def parse(tlaOutput: String): Dump[String] = {
    import Parsers.Dump._
    val content = Parsers.preprocess(tlaOutput)
    fastparse.parse(content, main(_)).get.value
  }
}