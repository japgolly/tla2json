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
    runWithTimeLimit(Duration.ofSeconds(2))(task)

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
          if (done)
            false
          else {
            done = true
            result = r
            if (r.isEmpty) {
              if (thread ne null) thread.interrupt()
            } else
              timer.cancel()
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

      thread = new Thread(taskRunnable)
      thread.start()

      val timeout = new TimerTask {
        override def run(): Unit = {
          complete(None)
        }
      }

      timer.schedule(timeout, maxDur.toMillis)

      sync.synchronized {
        sync.wait(maxDur.toMillis)
      }

      result
    } finally {
      timer.cancel()
    }
  }

}
