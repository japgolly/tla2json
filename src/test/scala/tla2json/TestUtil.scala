package tla2json

import io.circe.Json
import java.time.Duration
import java.util.{Timer, TimerTask}
import sourcecode.Line

object TestUtil extends japgolly.microlibs.testutil.TestUtil {

  implicit def univEqJson            : UnivEq[Json]      = UnivEq.force
  implicit def univEqValue           : UnivEq[Value]     = UnivEq.derive
  implicit def univEqState[A: UnivEq]: UnivEq[State[A]]  = UnivEq.derive
  implicit def univEqStepDesc        : UnivEq[Step.Desc] = UnivEq.derive
  implicit def univEqStep[A: UnivEq] : UnivEq[Step[A]]   = UnivEq.derive
  implicit def univEqSteps[A: UnivEq]: UnivEq[Steps[A]]  = UnivEq.derive

  def assertJson(actual: Json, expect: String)(implicit l: Line): Unit = {
    val e = io.circe.parser.parse(expect).getOrThrow()
    if (expect == "666")
      println(actual.noSpaces)
    assertEq(actual, e)
  }

  private def sysPropOrEnvVar(name: String): String =
    Option(System.getProperty(name)).orElse(Option(System.getenv(name)))
      .fold("")(_.trim.toLowerCase)

  private val inCI: Boolean =
    sysPropOrEnvVar("CI") == "1"

  if (inCI)
    println("============ CI MODE DETECTED ============")

  private val defaultTimeLimit: Duration =
    if (inCI)
      Duration.ofSeconds(2)
    else
      Duration.ofSeconds(30)

  def timeLimitedLazy[A](task: => A)(implicit l: Line): () => A = {
    val lock = new AnyRef
    var result = Option.empty[A]
    () =>
      result.getOrElse {
        lock.synchronized {
          result.getOrElse {
            val a = timeLimited(task)
            result = Some(a)
            a
          }
        }
      }
  }

  def timeLimited[A](task: => A)(implicit l: Line): A =
    runWithTimeLimit(defaultTimeLimit)(task)

  def runWithTimeLimit[A](maxDur: Duration)(task: => A)(implicit l: Line): A =
    runAttemptWithTimeLimit(maxDur)(task).getOrElse(fail("Task didn't complete within " + maxDur))

  def runAttemptWithTimeLimit[A](maxDur: Duration)(task: => A): Option[A] = {
    val lock   = new AnyRef
    val sync   = new AnyRef
    var result = Option.empty[A]
    var done   = false
    val timer  = new Timer("runAttemptWithTimeLimit", true)
    var thread = null : Thread

    def complete(r: Option[A]) = {
      val notify =
        lock.synchronized {
          if (r.isEmpty && thread != null) {
            thread.interrupt()
            thread = null
          }
          timer.cancel()
          if (done)
            false
          else {
            done = true
            result = r
            true
          }
        }

      if (notify)
        sync.synchronized {
          sync.notify()
        }
    }

    try {
      val taskRunnable: Runnable = () => {
        try {
          val a = task
          complete(Some(a))
        } catch {
          case _: InterruptedException =>
        } finally
          complete(None)
      }

      val timeout = new TimerTask {
        override def run(): Unit = {
          complete(None)
        }
      }

      timer.schedule(timeout, maxDur.toMillis)

      thread = new Thread(taskRunnable)
      thread.start()

      sync.synchronized {
        sync.wait(maxDur.toMillis)
      }

      result
    } finally {
      timer.cancel()
    }
  }

}
