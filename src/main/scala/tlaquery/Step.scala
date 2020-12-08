package tlaquery

import io.circe.Json

final case class Step[+A](no: Int, desc: Step.Desc, state: A) {

  def map[B](f: A => B): Step[B] =
    copy(state = f(state))

  def parseState(implicit ev: Step[A] <:< Step[String]): Step[State[Value]] =
    ev(this).map(State.parse(_).map(Value.parse))

  def toJson(implicit ev: A <:< State[Json]): Json =
    Json.obj(
      "no" -> Json.fromInt(no),
      "name" -> Json.fromString(desc.name),
      "state" -> state.toJson,
    )
}

object Step {

  sealed trait Desc {
    def name: String
  }

  object Desc {

    case object Initial extends Desc {
      override def name = "Initial predicate"
    }

    final case class Action(name: String) extends Desc

    case object Stuttering extends Desc {
      override def name = "Stuttering"
    }
  }
}
