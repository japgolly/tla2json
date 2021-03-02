package tla2json

import io.circe.Json
import scala.collection.immutable.ArraySeq

final case class Steps[+A](values: ArraySeq[Step[A]]) {

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
      val newSteps = new Array[Step[State[B]]](length)
      newSteps(0) = head
      for (i <- 1 until length) {
        val p = newSteps(i - 1).state.variables
        val step = self.values(i)
        val newVariables =
          allVars.foldLeft(step.state.variables)((s, v) =>
            if (s.contains(v))
              s
            else
              s.updated(v, p(v)))
        val newStep = step.copy(state = State(newVariables))
        newSteps(i) = newStep
      }
      Steps(ArraySeq.unsafeWrapArray(newSteps))
    }

  def withDiffStatePerStep[B](eql: (B, B) => Boolean, lastFull: Boolean)(implicit ev: Steps[A] <:< Steps[State[B]]): Steps[State[B]] =
    if (values.isEmpty)
      this
    else {
      val self = ev(this)
      val full = self.withFullStatePerStep.values
      val last = values.indices.last
      val newSteps = new Array[Step[State[B]]](length)
      for (i <- values.indices.iterator) {
        val step = self.values(i)
        val newStep =
          if (i == 0)
            step
          else if (lastFull && i == last)
            full.last
          else {
            val prev = full(i - 1).state.variables
            val newVars = step.state.variables.filter { case (k, v) => !eql(prev(k), v) }
            step.copy(state = State(newVars))
          }
        newSteps(i) = newStep
      }
      Steps(ArraySeq.unsafeWrapArray(newSteps))
    }

  def toJson(implicit ev: Steps[A] <:< Steps[State[Json]]): Json =
    Json.arr(ev(this).values.map(_.toJson): _*)
}

object Steps {

  type Parsed = Either[Steps[Value.Rec], Steps[String]]

  def parse(tlaOutput: String): Parsed = {
    import Parsers.Steps._
    var content = Parsers.preprocess(tlaOutput)
    if (isTraceFromToolboxErrorConsole(content))
      Left(fastparse.parse(content, traceFromToolboxErrorConsole(_)).get.value)
    else if (content.contains("Model checking completed. No error has been found."))
      Left(apply(ArraySeq.empty))
    else {
      content = preSteps.replaceFirstIn(content, "")
      Right(fastparse.parse(content, traceFromTlcOutput(_)).get.value)
    }
  }

  type Trace = Steps[State[Value]]

  def parseTrace(tlaOutput: String): Trace =
    parseTrace(parse(tlaOutput))

  def parseTrace(parsed: Parsed): Trace =
    parsed match {
      case Right(s) => s.map(_.parseState)
      case Left(s)  => s.map(_.unpackStateRecord)
    }

}