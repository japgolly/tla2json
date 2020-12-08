package tlaquery

import io.circe.Json

final case class Steps[+A](values: Vector[Step[A]]) {

  def isEmpty  = values.isEmpty
  def nonEmpty = values.nonEmpty
  def length   = values.length

  def map[B](f: Step[A] => Step[B]): Steps[B] =
    Steps(values.map(f))

  def mapSteps[B](f: A => B): Steps[B] =
    map(_.map(f))

  def mapValues[B, C](f: B => C)(implicit ev: Steps[A] <:< Steps[State[B]]): Steps[State[C]] =
    ev(this).mapSteps(_.map(f))

  /** no = number.
   * Get step #n where n≥1 as described by TLA+
   */
  def no(n: Int): Step[A] =
    values(n - 1)

  def withJsonValues(implicit ev: Steps[A] <:< Steps[State[Value]]): Steps[State[Json]] =
    ev(this).mapValues[Value, Json](_.toJson)

  def withFullStatePerStep[B](implicit ev: Steps[A] <:< Steps[State[B]]): Steps[State[B]] =
    if (values.isEmpty)
      this
    else {
      val self     = ev(this)
      val head     = self.values(0)
      val allVars  = head.state.variables.keySet
      val newSteps =
        self.values.tail.foldLeft(Vector(head)) { (prevSteps, step) =>
          val p = prevSteps.last.state.variables
          val newVariables =
            allVars.foldLeft(step.state.variables)((s, v) =>
              if (s.contains(v))
                s
              else
                s.updated(v, p(v)))
          val newStep = step.copy(state = State(newVariables))
          prevSteps :+ newStep
        }
      Steps(newSteps)
    }

  def toJson(implicit ev: Steps[A] <:< Steps[State[Json]]): Json =
    Json.arr(ev(this).values.map(_.toJson): _*)
}

object Steps {

  def parse(tlaOutput: String): Steps[String] = {
    import Parsers.Steps._
    var content = Parsers.preprocess(tlaOutput)
    if (content.contains("Model checking completed. No error has been found."))
      apply(Vector.empty)
    else {
      content = preSteps.replaceFirstIn(content, "")
      fastparse.parse(content, steps(_)).get.value
    }
  }

  type Trace = Steps[State[Value]]

  def parseTrace(tlaOutput: String): Trace =
    parse(tlaOutput).map(_.parseState)

}