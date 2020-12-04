package tlaquery

final case class Step[+A](no: Int, desc: Step.Desc, state: A)

object Step {

  sealed trait Desc
  object Desc {
    case object Initial extends Desc
    final case class Action(name: String) extends Desc
  }

}
