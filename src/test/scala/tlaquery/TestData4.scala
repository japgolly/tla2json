package tlaquery

object TestData4Test extends TestData.PropTest {
  override val testData = TestData4
  override def firstPostStateLine = "The number of states generated:"
  override def traceLines = {
    val ignoreLines = Set(110, 129, 130, 131, 132, 133, 134, 135)
    super.traceLines.linesIterator.zipWithIndex.filterNot(ignoreLines contains _._2).map(_._1).mkString("\n")
  }
}

object TestData4 extends TestData {

  val output =
    """
> cd /tmp/20201208-163217-1590259 && unbuffer tlc-colour -difftrace -simulate -seed 0 drafts
TLC2 Version 2.15 of Day Month 20?? (rev: e389d55)
Running Random Simulation with seed 0 with 1 worker on 24 cores with 25486MB heap and 64MB offheap memory [pid: 1590273] (Linux 5.9.11-arch2-1 amd64, GraalVM Community 11.0.8 x86_64).
Parsing file /tmp/20201208-163217-1590259/drafts.tla
Parsing file /tmp/FiniteSets.tla
Parsing file /tmp/Naturals.tla
Parsing file /tmp/Sequences.tla
Parsing file /tmp/TLC.tla
Parsing file /tmp/20201208-163217-1590259/Util.tla
Semantic processing of module Naturals
Semantic processing of module Sequences
Semantic processing of module FiniteSets
Semantic processing of module TLC
Semantic processing of module Util
Semantic processing of module drafts
Starting... (2020-12-08 16:32:17)
Computed 1 initial states...
"Error: Drafts are not eventually-consistent"
[ AB |-> {<<b1, ls>>},
  AW |-> {w1, w2},
  AT |-> {t1, t2},
  Stores |->
      { {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
        { [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)],
          [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] } } ]
Error: Invariant StableInvariants is violated.
Error: The behavior up to this point is:
State 1: <Initial predicate>
/\ browsers = ( b1 :> (ls :> [isEmpty |-> FALSE, get |-> {}]) @@
  b2 :> (ls :> [isEmpty |-> FALSE, get |-> {}]) )
/\ network = <<>>
/\ tabs = (t1 :> [status |-> "-"] @@ t2 :> [status |-> "-"])
/\ remote = {}
/\ workers = (w1 :> [status |-> "-"] @@ w2 :> [status |-> "-"])

State 2: <TabNew line 616, col 5 to line 634, col 10 of module drafts>
/\ tabs = ( t1 :> [status |-> "-"] @@
  t2 :>
      [ drafts |-> {},
        worker |-> w2,
        status |-> "loading",
        awaiting |-> {ls, "Remote"} ] )
/\ workers = ( w1 :> [status |-> "-"] @@
  w2 :>
      [ drafts |-> {},
        time |-> 1,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 0, lastReq |-> 0, lastAck |-> 0]] ] )

State 3: <TabNew line 616, col 5 to line 634, col 10 of module drafts>
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

State 4: <TabLoad line 592, col 5 to line 612, col 67 of module drafts>
/\ tabs = ( t1 :>
      [drafts |-> {}, worker |-> w1, status |-> "loading", awaiting |-> {ls}] @@
  t2 :>
      [ drafts |-> {},
        worker |-> w2,
        status |-> "loading",
        awaiting |-> {ls, "Remote"} ] )

State 5: <TabLoad line 592, col 5 to line 612, col 67 of module drafts>
/\ tabs = ( t1 :>
      [drafts |-> {}, worker |-> w1, status |-> "loading", awaiting |-> {ls}] @@
  t2 :>
      [drafts |-> {}, worker |-> w2, status |-> "loading", awaiting |-> {ls}] )

State 6: <TabLoad line 592, col 5 to line 612, col 67 of module drafts>
/\ tabs = ( t1 :> [drafts |-> {}, worker |-> w1, status |-> "loading", awaiting |-> {}] @@
  t2 :>
      [drafts |-> {}, worker |-> w2, status |-> "loading", awaiting |-> {ls}] )

State 7: <TabStart line 646, col 7 to line 659, col 50 of module drafts>
/\ tabs = ( t1 :> [worker |-> w1, status |-> "clean"] @@
  t2 :>
      [drafts |-> {}, worker |-> w2, status |-> "loading", awaiting |-> {ls}] )

State 8: <TabLoad line 592, col 5 to line 612, col 67 of module drafts>
/\ tabs = ( t1 :> [worker |-> w1, status |-> "clean"] @@
  t2 :> [drafts |-> {}, worker |-> w2, status |-> "loading", awaiting |-> {}] )

State 9: <TabStart line 646, col 7 to line 659, col 50 of module drafts>
/\ tabs = ( t1 :> [worker |-> w1, status |-> "clean"] @@
  t2 :> [worker |-> w2, status |-> "clean"] )

State 10: <UserEditClean line 721, col 7 to line 723, col 59 of module drafts>
/\ tabs = ( t1 :>
      [ drafts |-> {},
        worker |-> w1,
        status |-> "dirty",
        editDraft |-> [isEmpty |-> TRUE],
        editRev |-> 1,
        editRevSent |-> 0,
        editCount |-> 1 ] @@
  t2 :> [worker |-> w2, status |-> "clean"] )

State 11: <TabSendChangesToWorker line 700, col 7 to line 713, col 50 of module drafts>
/\ network = << [ drafts |-> {},
     type |-> "sync:T->W",
     from |-> t1,
     to |-> w1,
     edit |->
         [ isEmpty |-> FALSE,
           get |-> [prov |-> (w1 :> 0 @@ w2 :> 0), rev |-> 1] ] ] >>
/\ tabs = ( t1 :>
      [ drafts |-> {},
        worker |-> w1,
        status |-> "dirty",
        editDraft |-> [isEmpty |-> TRUE],
        editRev |-> 1,
        editRevSent |-> 1,
        editCount |-> 1 ] @@
  t2 :> [worker |-> w2, status |-> "clean"] )

"Error: Drafts are not eventually-consistent"
State 12: <UserEditClean line 721, col 7 to line 723, col 59 of module drafts>
/\ tabs = ( t1 :>
      [ drafts |-> {},
        worker |-> w1,
        status |-> "dirty",
        editDraft |-> [isEmpty |-> TRUE],
        editRev |-> 1,
        editRevSent |-> 1,
        editCount |-> 1 ] @@
  t2 :>
      [ drafts |-> {},
        worker |-> w2,
        status |-> "dirty",
        editDraft |-> [isEmpty |-> TRUE],
        editRev |-> 1,
        editRevSent |-> 0,
        editCount |-> 1 ] )

[ AB |-> {<<b1, ls>>},
  AW |-> {w1, w2},
  AT |-> {t1, t2},
  Stores |->
      { {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
        { [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)],
          [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] } } ]
State 13: <TabSendChangesToWorker line 700, col 7 to line 713, col 50 of module drafts>
/\ network = << [ drafts |-> {},
     type |-> "sync:T->W",
     from |-> t1,
     to |-> w1,
     edit |->
         [ isEmpty |-> FALSE,
           get |-> [prov |-> (w1 :> 0 @@ w2 :> 0), rev |-> 1] ] ],
   [ drafts |-> {},
     type |-> "sync:T->W",
     from |-> t2,
     to |-> w2,
     edit |->
         [ isEmpty |-> FALSE,
           get |-> [prov |-> (w1 :> 0 @@ w2 :> 0), rev |-> 1] ] ] >>
/\ tabs = ( t1 :>
      [ drafts |-> {},
        worker |-> w1,
        status |-> "dirty",
        editDraft |-> [isEmpty |-> TRUE],
        editRev |-> 1,
        editRevSent |-> 1,
        editCount |-> 1 ] @@
  t2 :>
      [ drafts |-> {},
        worker |-> w2,
        status |-> "dirty",
        editDraft |-> [isEmpty |-> TRUE],
        editRev |-> 1,
        editRevSent |-> 1,
        editCount |-> 1 ] )

State 14: <WorkerRecvChanges line 752, col 5 to line 773, col 53 of module drafts>
/\ network = << [ drafts |-> {},
     type |-> "sync:T->W",
     from |-> t2,
     to |-> w2,
     edit |->
         [ isEmpty |-> FALSE,
           get |-> [prov |-> (w1 :> 0 @@ w2 :> 0), rev |-> 1] ] ],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:W->T",
     from |-> w1,
     to |-> t1,
     edit |->
         [ isEmpty |-> FALSE,
           get |->
               [ rev |-> 1,
                 draft |->
                     [ worker |-> w1,
                       time |-> 1,
                       prov |-> (w1 :> 0 @@ w2 :> 0) ] ] ] ] >>
/\ workers = ( w1 :>
      [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
        time |-> 2,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 1, lastReq |-> 0, lastAck |-> 0]] ] @@
  w2 :>
      [ drafts |-> {},
        time |-> 1,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 0, lastReq |-> 0, lastAck |-> 0]] ] )

State 15: <WorkerRecvChanges line 752, col 5 to line 773, col 53 of module drafts>
/\ network = << [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:W->T",
     from |-> w1,
     to |-> t1,
     edit |->
         [ isEmpty |-> FALSE,
           get |->
               [ rev |-> 1,
                 draft |->
                     [ worker |-> w1,
                       time |-> 1,
                       prov |-> (w1 :> 0 @@ w2 :> 0) ] ] ] ],
   [ drafts |-> {[worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:W->T",
     from |-> w2,
     to |-> t2,
     edit |->
         [ isEmpty |-> FALSE,
           get |->
               [ rev |-> 1,
                 draft |->
                     [ worker |-> w2,
                       time |-> 1,
                       prov |-> (w1 :> 0 @@ w2 :> 0) ] ] ] ] >>
/\ workers = ( w1 :>
      [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
        time |-> 2,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 1, lastReq |-> 0, lastAck |-> 0]] ] @@
  w2 :>
      [ drafts |-> {[worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
        time |-> 2,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 1, lastReq |-> 0, lastAck |-> 0]] ] )

State 16: <WorkerSyncWithBrowserStorage line 783, col 5 to line 800, col 39 of module drafts>
/\ browsers = ( b1 :>
      ( ls :>
            [ isEmpty |-> FALSE,
              get |->
                  { [ worker |-> w2,
                      time |-> 1,
                      prov |-> (w1 :> 0 @@ w2 :> 0) ] } ] ) @@
  b2 :> (ls :> [isEmpty |-> FALSE, get |-> {}]) )

State 17: <WorkerSendRemoteStoreCmd line 816, col 7 to line 822, col 49 of module drafts>
/\ network = << [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:W->T",
     from |-> w1,
     to |-> t1,
     edit |->
         [ isEmpty |-> FALSE,
           get |->
               [ rev |-> 1,
                 draft |->
                     [ worker |-> w1,
                       time |-> 1,
                       prov |-> (w1 :> 0 @@ w2 :> 0) ] ] ] ],
   [ drafts |-> {[worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:W->T",
     from |-> w2,
     to |-> t2,
     edit |->
         [ isEmpty |-> FALSE,
           get |->
               [ rev |-> 1,
                 draft |->
                     [ worker |-> w2,
                       time |-> 1,
                       prov |-> (w1 :> 0 @@ w2 :> 0) ] ] ] ],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "cmd:T->R",
     from |-> w1,
     to |-> t1,
     id |-> 1 ] >>
/\ workers = ( w1 :>
      [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
        time |-> 2,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 1, lastReq |-> 1, lastAck |-> 0]] ] @@
  w2 :>
      [ drafts |-> {[worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
        time |-> 2,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 1, lastReq |-> 0, lastAck |-> 0]] ] )

State 18: <WorkerSendRemoteStoreCmd line 816, col 7 to line 822, col 49 of module drafts>
/\ network = << [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:W->T",
     from |-> w1,
     to |-> t1,
     edit |->
         [ isEmpty |-> FALSE,
           get |->
               [ rev |-> 1,
                 draft |->
                     [ worker |-> w1,
                       time |-> 1,
                       prov |-> (w1 :> 0 @@ w2 :> 0) ] ] ] ],
   [ drafts |-> {[worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:W->T",
     from |-> w2,
     to |-> t2,
     edit |->
         [ isEmpty |-> FALSE,
           get |->
               [ rev |-> 1,
                 draft |->
                     [ worker |-> w2,
                       time |-> 1,
                       prov |-> (w1 :> 0 @@ w2 :> 0) ] ] ] ],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "cmd:T->R",
     from |-> w1,
     to |-> t1,
     id |-> 1 ],
   [ drafts |-> {[worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "cmd:T->R",
     from |-> w2,
     to |-> t2,
     id |-> 1 ] >>
/\ workers = ( w1 :>
      [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
        time |-> 2,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 1, lastReq |-> 1, lastAck |-> 0]] ] @@
  w2 :>
      [ drafts |-> {[worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
        time |-> 2,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 1, lastReq |-> 1, lastAck |-> 0]] ] )

State 19: <TabRecvRemoteStoreCmd line 664, col 5 to line 694, col 54 of module drafts>
/\ network = << [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:W->T",
     from |-> w1,
     to |-> t1,
     edit |->
         [ isEmpty |-> FALSE,
           get |->
               [ rev |-> 1,
                 draft |->
                     [ worker |-> w1,
                       time |-> 1,
                       prov |-> (w1 :> 0 @@ w2 :> 0) ] ] ] ],
   [ drafts |-> {[worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:W->T",
     from |-> w2,
     to |-> t2,
     edit |->
         [ isEmpty |-> FALSE,
           get |->
               [ rev |-> 1,
                 draft |->
                     [ worker |-> w2,
                       time |-> 1,
                       prov |-> (w1 :> 0 @@ w2 :> 0) ] ] ] ],
   [ drafts |-> {[worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "cmd:T->R",
     from |-> w2,
     to |-> t2,
     id |-> 1 ],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:T->R",
     from |-> t1,
     to |-> "Remote",
     id |-> 1 ] >>
/\ tabs = ( t1 :>
      [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
        worker |-> w1,
        status |-> "dirty",
        editDraft |-> [isEmpty |-> TRUE],
        editRev |-> 1,
        editRevSent |-> 1,
        editCount |-> 1 ] @@
  t2 :>
      [ drafts |-> {},
        worker |-> w2,
        status |-> "dirty",
        editDraft |-> [isEmpty |-> TRUE],
        editRev |-> 1,
        editRevSent |-> 1,
        editCount |-> 1 ] )

State 20: <TabRecvDraftsFromWorker line 579, col 5 to line 588, col 52 of module drafts>
/\ network = << [ drafts |-> {[worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:W->T",
     from |-> w2,
     to |-> t2,
     edit |->
         [ isEmpty |-> FALSE,
           get |->
               [ rev |-> 1,
                 draft |->
                     [ worker |-> w2,
                       time |-> 1,
                       prov |-> (w1 :> 0 @@ w2 :> 0) ] ] ] ],
   [ drafts |-> {[worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "cmd:T->R",
     from |-> w2,
     to |-> t2,
     id |-> 1 ],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:T->R",
     from |-> t1,
     to |-> "Remote",
     id |-> 1 ] >>
/\ tabs = ( t1 :>
      [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
        worker |-> w1,
        status |-> "dirty",
        editDraft |-> [isEmpty |-> TRUE],
        editRev |-> 0,
        editRevSent |-> 0,
        editCount |-> 1 ] @@
  t2 :>
      [ drafts |-> {},
        worker |-> w2,
        status |-> "dirty",
        editDraft |-> [isEmpty |-> TRUE],
        editRev |-> 1,
        editRevSent |-> 1,
        editCount |-> 1 ] )

State 21: <TabRecvRemoteStoreCmd line 664, col 5 to line 694, col 54 of module drafts>
/\ network = << [ drafts |-> {[worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:W->T",
     from |-> w2,
     to |-> t2,
     edit |->
         [ isEmpty |-> FALSE,
           get |->
               [ rev |-> 1,
                 draft |->
                     [ worker |-> w2,
                       time |-> 1,
                       prov |-> (w1 :> 0 @@ w2 :> 0) ] ] ] ],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:T->R",
     from |-> t1,
     to |-> "Remote",
     id |-> 1 ],
   [ drafts |-> {[worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:T->R",
     from |-> t2,
     to |-> "Remote",
     id |-> 1 ] >>
/\ tabs = ( t1 :>
      [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
        worker |-> w1,
        status |-> "dirty",
        editDraft |-> [isEmpty |-> TRUE],
        editRev |-> 0,
        editRevSent |-> 0,
        editCount |-> 1 ] @@
  t2 :>
      [ drafts |-> {[worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
        worker |-> w2,
        status |-> "dirty",
        editDraft |-> [isEmpty |-> TRUE],
        editRev |-> 1,
        editRevSent |-> 1,
        editCount |-> 1 ] )

State 22: <TabRecvDraftsFromWorker line 579, col 5 to line 588, col 52 of module drafts>
/\ network = << [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:T->R",
     from |-> t1,
     to |-> "Remote",
     id |-> 1 ],
   [ drafts |-> {[worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:T->R",
     from |-> t2,
     to |-> "Remote",
     id |-> 1 ] >>
/\ tabs = ( t1 :>
      [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
        worker |-> w1,
        status |-> "dirty",
        editDraft |-> [isEmpty |-> TRUE],
        editRev |-> 0,
        editRevSent |-> 0,
        editCount |-> 1 ] @@
  t2 :>
      [ drafts |-> {[worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
        worker |-> w2,
        status |-> "dirty",
        editDraft |-> [isEmpty |-> TRUE],
        editRev |-> 0,
        editRevSent |-> 0,
        editCount |-> 1 ] )

State 23: <WorkerSyncWithBrowserStorage line 783, col 5 to line 800, col 39 of module drafts>
/\ browsers = ( b1 :>
      ( ls :>
            [ isEmpty |-> FALSE,
              get |->
                  { [ worker |-> w1,
                      time |-> 1,
                      prov |-> (w1 :> 0 @@ w2 :> 1) ] } ] ) @@
  b2 :> (ls :> [isEmpty |-> FALSE, get |-> {}]) )
/\ network = << [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:T->R",
     from |-> t1,
     to |-> "Remote",
     id |-> 1 ],
   [ drafts |-> {[worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:T->R",
     from |-> t2,
     to |-> "Remote",
     id |-> 1 ],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
     type |-> "sync:W->T",
     from |-> w1,
     to |-> t1,
     edit |-> [isEmpty |-> TRUE] ] >>
/\ workers = ( w1 :>
      [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
        time |-> 2,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 1, lastReq |-> 1, lastAck |-> 0]] ] @@
  w2 :>
      [ drafts |-> {[worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
        time |-> 2,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 1, lastReq |-> 1, lastAck |-> 0]] ] )

State 24: <WorkerSyncWithBrowserStorage line 783, col 5 to line 800, col 39 of module drafts>
/\ network = << [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:T->R",
     from |-> t1,
     to |-> "Remote",
     id |-> 1 ],
   [ drafts |-> {[worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:T->R",
     from |-> t2,
     to |-> "Remote",
     id |-> 1 ],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
     type |-> "sync:W->T",
     from |-> w1,
     to |-> t1,
     edit |-> [isEmpty |-> TRUE] ],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
     type |-> "sync:W->T",
     from |-> w2,
     to |-> t2,
     edit |-> [isEmpty |-> TRUE] ] >>
/\ workers = ( w1 :>
      [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
        time |-> 2,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 1, lastReq |-> 1, lastAck |-> 0]] ] @@
  w2 :>
      [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
        time |-> 2,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 1, lastReq |-> 1, lastAck |-> 0]] ] )

State 25: <RemoteRecvDrafts line 530, col 5 to line 553, col 50 of module drafts>
/\ network = << [ drafts |-> {[worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:T->R",
     from |-> t2,
     to |-> "Remote",
     id |-> 1 ],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
     type |-> "sync:W->T",
     from |-> w1,
     to |-> t1,
     edit |-> [isEmpty |-> TRUE] ],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
     type |-> "sync:W->T",
     from |-> w2,
     to |-> t2,
     edit |-> [isEmpty |-> TRUE] ],
   [type |-> "ack:R->T", from |-> "Remote", to |-> t1, id |-> 1],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:R->T",
     from |-> "Remote",
     to |-> t2 ] >>
/\ remote = {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]}

State 26: <TabRecvDraftsFromWorker line 579, col 5 to line 588, col 52 of module drafts>
/\ network = << [ drafts |-> {[worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:T->R",
     from |-> t2,
     to |-> "Remote",
     id |-> 1 ],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
     type |-> "sync:W->T",
     from |-> w2,
     to |-> t2,
     edit |-> [isEmpty |-> TRUE] ],
   [type |-> "ack:R->T", from |-> "Remote", to |-> t1, id |-> 1],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:R->T",
     from |-> "Remote",
     to |-> t2 ] >>
/\ tabs = ( t1 :>
      [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
        worker |-> w1,
        status |-> "dirty",
        editDraft |-> [isEmpty |-> TRUE],
        editRev |-> 0,
        editRevSent |-> 0,
        editCount |-> 1 ] @@
  t2 :>
      [ drafts |-> {[worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
        worker |-> w2,
        status |-> "dirty",
        editDraft |-> [isEmpty |-> TRUE],
        editRev |-> 0,
        editRevSent |-> 0,
        editCount |-> 1 ] )

State 27: <RemoteRecvDrafts line 530, col 5 to line 553, col 50 of module drafts>
/\ network = << [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
     type |-> "sync:W->T",
     from |-> w2,
     to |-> t2,
     edit |-> [isEmpty |-> TRUE] ],
   [type |-> "ack:R->T", from |-> "Remote", to |-> t1, id |-> 1],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:R->T",
     from |-> "Remote",
     to |-> t2 ],
   [type |-> "ack:R->T", from |-> "Remote", to |-> t2, id |-> 1],
   [ drafts |->
         { [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)],
           [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] },
     type |-> "sync:R->T",
     from |-> "Remote",
     to |-> t1 ] >>
/\ remote = { [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)],
  [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] }

State 28: <TabRecvDraftsFromWorker line 579, col 5 to line 588, col 52 of module drafts>
/\ network = << [type |-> "ack:R->T", from |-> "Remote", to |-> t1, id |-> 1],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:R->T",
     from |-> "Remote",
     to |-> t2 ],
   [type |-> "ack:R->T", from |-> "Remote", to |-> t2, id |-> 1],
   [ drafts |->
         { [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)],
           [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] },
     type |-> "sync:R->T",
     from |-> "Remote",
     to |-> t1 ] >>
/\ tabs = ( t1 :>
      [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
        worker |-> w1,
        status |-> "dirty",
        editDraft |-> [isEmpty |-> TRUE],
        editRev |-> 0,
        editRevSent |-> 0,
        editCount |-> 1 ] @@
  t2 :>
      [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
        worker |-> w2,
        status |-> "dirty",
        editDraft |-> [isEmpty |-> TRUE],
        editRev |-> 0,
        editRevSent |-> 0,
        editCount |-> 1 ] )

State 29: <TabRecvRemoteAck line 827, col 5 to line 838, col 58 of module drafts>
/\ network = << [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:R->T",
     from |-> "Remote",
     to |-> t2 ],
   [type |-> "ack:R->T", from |-> "Remote", to |-> t2, id |-> 1],
   [ drafts |->
         { [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)],
           [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] },
     type |-> "sync:R->T",
     from |-> "Remote",
     to |-> t1 ],
   [type |-> "ack:T->W", from |-> t1, to |-> w1, id |-> 1] >>

State 30: <TabRecvDraftsFromRemote line 558, col 5 to line 574, col 52 of module drafts>
/\ network = << [type |-> "ack:R->T", from |-> "Remote", to |-> t2, id |-> 1],
   [ drafts |->
         { [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)],
           [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] },
     type |-> "sync:R->T",
     from |-> "Remote",
     to |-> t1 ],
   [type |-> "ack:T->W", from |-> t1, to |-> w1, id |-> 1],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
     type |-> "sync:T->W",
     from |-> t2,
     to |-> w2,
     edit |-> [isEmpty |-> TRUE] ] >>

State 31: <WorkerRecvChanges line 752, col 5 to line 773, col 53 of module drafts>
/\ network = << [type |-> "ack:R->T", from |-> "Remote", to |-> t2, id |-> 1],
   [ drafts |->
         { [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)],
           [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] },
     type |-> "sync:R->T",
     from |-> "Remote",
     to |-> t1 ],
   [type |-> "ack:T->W", from |-> t1, to |-> w1, id |-> 1] >>

State 32: <WorkerRecvRemoteAck line 843, col 5 to line 850, col 49 of module drafts>
/\ network = << [type |-> "ack:R->T", from |-> "Remote", to |-> t2, id |-> 1],
   [ drafts |->
         { [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)],
           [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] },
     type |-> "sync:R->T",
     from |-> "Remote",
     to |-> t1 ] >>
/\ workers = ( w1 :>
      [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
        time |-> 2,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 1, lastReq |-> 1, lastAck |-> 1]] ] @@
  w2 :>
      [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
        time |-> 2,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 1, lastReq |-> 1, lastAck |-> 0]] ] )

State 33: <TabRecvRemoteAck line 827, col 5 to line 838, col 58 of module drafts>
/\ network = << [ drafts |->
         { [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)],
           [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] },
     type |-> "sync:R->T",
     from |-> "Remote",
     to |-> t1 ],
   [type |-> "ack:T->W", from |-> t2, to |-> w2, id |-> 1] >>

State 34: <WorkerRecvRemoteAck line 843, col 5 to line 850, col 49 of module drafts>
/\ network = << [ drafts |->
         { [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)],
           [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] },
     type |-> "sync:R->T",
     from |-> "Remote",
     to |-> t1 ] >>
/\ workers = ( w1 :>
      [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
        time |-> 2,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 1, lastReq |-> 1, lastAck |-> 1]] ] @@
  w2 :>
      [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
        time |-> 2,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 1, lastReq |-> 1, lastAck |-> 1]] ] )

State 35: <TabRecvDraftsFromRemote line 558, col 5 to line 574, col 52 of module drafts>
/\ network = << [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
     type |-> "sync:T->W",
     from |-> t1,
     to |-> w1,
     edit |-> [isEmpty |-> TRUE] ] >>

State 36: <WorkerRecvChanges line 752, col 5 to line 773, col 53 of module drafts>
/\ network = <<>>

The number of states generated: 659
Simulation using seed 0 and aril 0
Progress: 659 states checked, 14 traces generated (trace length: mean=39, var(x)=8, sd=3)
Finished in 00s at (2020-12-08 16:32:18)
    """.stripMargin

}
