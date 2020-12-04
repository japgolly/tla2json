package tlaquery

import java.time.Duration
import java.util.{Timer, TimerTask}
import sourcecode.Line

object TestUtil extends japgolly.microlibs.testutil.TestUtil {

  implicit def univEqStepDesc: UnivEq[Step.Desc] = UnivEq.derive
  implicit def univEqValue: UnivEq[Value] = UnivEq.derive

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
