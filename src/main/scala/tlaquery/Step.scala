package tlaquery

final case class Step[+A](no: Int, desc: Step.Desc, state: A) {

  def map[B](f: A => B): Step[B] =
    copy(state = f(state))
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
}
