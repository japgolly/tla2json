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
}
