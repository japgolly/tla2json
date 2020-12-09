package tla2json

object TestData3Test extends TestData.PropTest {
  override val testData = TestData3
  override def firstPostStateLine = "Finished checking temporal properties"
}

object TestData3 extends TestData {

  val output =
    """
> cd /tmp/20201208-123341-1544857 && unbuffer tlc-colour -difftrace drafts
TLC2 Version 2.15 of Day Month 20?? (rev: e389d55)
Running breadth-first search Model-Checking with fp 70 and seed 155913647436926390 with 24 workers on 24 cores with 25486MB heap and 64MB offheap memory [pid: 1544871] (Linux 5.9.11-arch2-1 amd64, GraalVM Community 11.0.8 x86_64, MSBDiskFPSet, DiskStateQueue).
Parsing file /tmp/20201208-123341-1544857/drafts.tla
Parsing file /tmp/FiniteSets.tla
Parsing file /tmp/Naturals.tla
Parsing file /tmp/Sequences.tla
Parsing file /tmp/TLC.tla
Parsing file /tmp/20201208-123341-1544857/Util.tla
Semantic processing of module Naturals
Semantic processing of module Sequences
Semantic processing of module FiniteSets
Semantic processing of module TLC
Semantic processing of module Util
Semantic processing of module drafts
Starting... (2020-12-08 12:33:42)
Implied-temporal checking--satisfiability problem has 1 branches.
Computing initial states...
Finished computing initial states: 1 distinct state generated at 2020-12-08 12:33:42.
Checking temporal properties for the current state space with 4992 total distinct states at (2020-12-08 12:33:45)
Error: Temporal properties were violated.

Error: The following behavior constitutes a counter-example:

State 1: <Initial predicate>
/\ browsers = ( b1 :> (ls :> [isEmpty |-> FALSE, get |-> {}]) @@
  b2 :> (ls :> [isEmpty |-> FALSE, get |-> {}]) )
/\ network = <<>>
/\ tabs = (t1 :> [status |-> "-"] @@ t2 :> [status |-> "-"])
/\ remote = {}
/\ workers = (w1 :> [status |-> "-"] @@ w2 :> [status |-> "-"])

State 2: <TabNew line 614, col 5 to line 632, col 10 of module drafts>
/\ tabs = ( t1 :>
      [ drafts |-> {},
        worker |-> w1,
        status |-> "loading",
        awaiting |-> {ls, "Remote"} ] @@
  t2 :> [status |-> "-"] )
/\ workers = ( w1 :>
      [ drafts |-> {},
        time |-> 1,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 0, lastReq |-> 0, lastAck |-> 0]] ] @@
  w2 :> [status |-> "-"] )

State 3: <TabNew line 614, col 5 to line 632, col 10 of module drafts>
/\ tabs = ( t1 :>
      [ drafts |-> {},
        worker |-> w1,
        status |-> "loading",
        awaiting |-> {ls, "Remote"} ] @@
  t2 :>
      [ drafts |-> {},
        worker |-> w2,
        status |-> "loading",
        awaiting |-> {ls, "Remote"} ] )
/\ workers = ( w1 :>
      [ drafts |-> {},
        time |-> 1,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 0, lastReq |-> 0, lastAck |-> 0]] ] @@
  w2 :>
      [ drafts |-> {},
        time |-> 1,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 0, lastReq |-> 0, lastAck |-> 0]] ] )

State 4: <TabLoad line 590, col 5 to line 610, col 67 of module drafts>
/\ tabs = ( t1 :>
      [ drafts |-> {},
        worker |-> w1,
        status |-> "loading",
        awaiting |-> {"Remote"} ] @@
  t2 :>
      [ drafts |-> {},
        worker |-> w2,
        status |-> "loading",
        awaiting |-> {ls, "Remote"} ] )

State 5: <TabLoad line 590, col 5 to line 610, col 67 of module drafts>
/\ tabs = ( t1 :>
      [ drafts |-> {},
        worker |-> w1,
        status |-> "loading",
        awaiting |-> {"Remote"} ] @@
  t2 :>
      [drafts |-> {}, worker |-> w2, status |-> "loading", awaiting |-> {ls}] )

State 6: <TabLoad line 590, col 5 to line 610, col 67 of module drafts>
/\ tabs = ( t1 :> [drafts |-> {}, worker |-> w1, status |-> "loading", awaiting |-> {}] @@
  t2 :>
      [drafts |-> {}, worker |-> w2, status |-> "loading", awaiting |-> {ls}] )

State 7: <TabLoad line 590, col 5 to line 610, col 67 of module drafts>
/\ tabs = ( t1 :> [drafts |-> {}, worker |-> w1, status |-> "loading", awaiting |-> {}] @@
  t2 :> [drafts |-> {}, worker |-> w2, status |-> "loading", awaiting |-> {}] )

State 8: <TabStart line 644, col 7 to line 657, col 50 of module drafts>
/\ tabs = ( t1 :> [worker |-> w1, status |-> "clean"] @@
  t2 :> [drafts |-> {}, worker |-> w2, status |-> "loading", awaiting |-> {}] )

State 9: <TabStart line 644, col 7 to line 657, col 50 of module drafts>
/\ tabs = ( t1 :> [worker |-> w1, status |-> "clean"] @@
  t2 :> [worker |-> w2, status |-> "clean"] )

State 10: Stuttering
Finished checking temporal properties in 00s at 2020-12-08 12:33:45
28111 states generated, 10469 distinct states found, 5477 states left on queue.
The depth of the complete state graph search is 21.
Finished in 03s at (2020-12-08 12:33:45)
    """.stripMargin

}
