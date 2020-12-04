package tlaquery

final case class Step[+A](no: Int, desc: Step.Desc, state: A) {

  def map[B](f: A => B): Step[B] =
    copy(state = f(state))

  def parseState(implicit ev: Step[A] <:< Step[String]): Step[State[Value]] =
    ev(this).map(State.parse(_).map(Value.parse))
}

object Step {

  sealed trait Desc

  object Desc {
    case object Initial extends Desc
    final case class Action(name: String) extends Desc
  }

  def parseMany(tlaOutput: String): Vector[Step[String]] = {
    import Parsers.Steps._
    var content = Parsers.preprocess(tlaOutput)
    content = preSteps.replaceFirstIn(content, "")
    fastparse.parse(content, steps(_)).get.value
  }

  type Trace = Vector[Step[State[Value]]]

  def parseTrace(tlaOutput: String): Trace =
    parseMany(tlaOutput).map(_.parseState)
}
