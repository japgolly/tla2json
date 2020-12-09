package tla2json

import io.circe.Json
import japgolly.microlibs.stdlib_ext.StdlibExt._
import scala.collection.immutable.VectorMap

final case class State[+A](variables: VectorMap[String, A]) {

  def map[B](f: A => B): State[B] =
    State(variables.iterator.map(_.map2(f)).to(VectorMap))

  def toJson(implicit f: A => Json): Json =
    Json.obj(variables.iterator.map(_.map2(f)).toSeq: _*)
}

object State {

  def parse(s: String): State[String] =
    fastparse.parse(
      Parsers.preprocess(s),
      Parsers.States.main(_)
    ).get.value

}