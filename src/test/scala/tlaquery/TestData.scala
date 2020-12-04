package tlaquery

import TestUtil._

abstract class TestData {

  val output: String

  final val stepsStr: () => Vector[Step[String]] =
    timeLimitedLazy(Step.parseMany(output))
}
