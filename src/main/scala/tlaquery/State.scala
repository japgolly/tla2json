package tlaquery

import japgolly.microlibs.stdlib_ext.StdlibExt._

final case class State[+A](variables: Map[String, A]) {

  def map[B](f: A => B): State[B] =
    State(variables.mapValuesNow(f))
}

object State {

  def parse(s: String): State[String] =
    fastparse.parse(
      Parsers.preprocess(s),
      Parsers.States.main(_)
    ).get.value

}