package tlaquery

import japgolly.microlibs.stdlib_ext.StdlibExt._
import scala.collection.immutable.VectorMap

final case class State[+A](variables: VectorMap[String, A]) {

  def map[B](f: A => B): State[B] =
    State(variables.iterator.map(_.map2(f)).to(VectorMap))
}

object State {

  def parse(s: String): State[String] =
    fastparse.parse(
      Parsers.preprocess(s),
      Parsers.States.main(_)
    ).get.value

}