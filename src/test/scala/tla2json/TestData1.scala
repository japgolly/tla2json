package tla2json

object TestData1Test extends TestData.PropTest {
  override val testData = TestData1
  override def firstPostStateLine = "The number of states generated:"
}

object TestData1 extends TestData {

  val output =
    """
TLC2 Version 2.15 of Day Month 20?? (rev: eb3ff99)
Running Random Simulation with seed 0 with 1 worker on 24 cores with 25486MB heap and 64MB offheap memory [pid: 685366] (Linux 5.9.11-arch2-1 amd64, GraalVM Community 11.0.8 x86_64).
Parsing file /tmp/20201204-125637-685358/drafts.tla
Parsing file /tmp/FiniteSets.tla
Parsing file /tmp/Naturals.tla
Parsing file /tmp/Sequences.tla
Parsing file /tmp/TLC.tla
Parsing file /tmp/20201204-125637-685358/Util.tla
Semantic processing of module Naturals
Semantic processing of module Sequences
Semantic processing of module FiniteSets
Semantic processing of module TLC
Semantic processing of module Util
Semantic processing of module drafts
Starting... (2020-12-04 12:56:37)
Implied-temporal checking--satisfiability problem has 1 branches.
Computed 1 initial states...
"Error: Drafts are not eventually-consistent"
[ AB |-> {},
  AW |-> {w1, w2},
  AT |-> {t1, t2},
  Stores |->
      { {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
        { [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)],
          [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] },
        { [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)],
          [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] } } ]
Error: Invariant StableInvariants is violated.
Error: The behavior up to this point is:
State 1: <Initial predicate>
/\ browsers = (b1 :> << >>)
/\ network = <<>>
/\ tabs = (t1 :> [status |-> "-"] @@ t2 :> [status |-> "-"])
/\ remote = {}
/\ workers = (w1 :> [status |-> "-"] @@ w2 :> [status |-> "-"])

State 2: <TabNew line 581, col 5 to line 595, col 10 of module drafts>
/\ tabs = ( t1 :> [status |-> "-"] @@
  t2 :>
      [ drafts |-> {},
        worker |-> w1,
        status |-> "loading",
        awaiting |-> {"Remote"} ] )
/\ workers = ( w1 :>
      [ drafts |-> {},
        time |-> 1,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 0, lastReq |-> 0, lastAck |-> 0]] ] @@
  w2 :> [status |-> "-"] )

State 3: <TabNew line 581, col 5 to line 595, col 10 of module drafts>
/\ tabs = ( t1 :>
      [ drafts |-> {},
        worker |-> w2,
        status |-> "loading",
        awaiting |-> {"Remote"} ] @@
  t2 :>
      [ drafts |-> {},
        worker |-> w1,
        status |-> "loading",
        awaiting |-> {"Remote"} ] )
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

State 4: <TabLoad line 557, col 5 to line 577, col 67 of module drafts>
/\ tabs = ( t1 :>
      [ drafts |-> {},
        worker |-> w2,
        status |-> "loading",
        awaiting |-> {"Remote"} ] @@
  t2 :> [drafts |-> {}, worker |-> w1, status |-> "loading", awaiting |-> {}] )

State 5: <TabLoad line 557, col 5 to line 577, col 67 of module drafts>
/\ tabs = ( t1 :> [drafts |-> {}, worker |-> w2, status |-> "loading", awaiting |-> {}] @@
  t2 :> [drafts |-> {}, worker |-> w1, status |-> "loading", awaiting |-> {}] )

State 6: <TabStart line 602, col 7 to line 614, col 50 of module drafts>
/\ tabs = ( t1 :> [drafts |-> {}, worker |-> w2, status |-> "loading", awaiting |-> {}] @@
  t2 :> [worker |-> w1, status |-> "clean"] )

State 7: <TabStart line 602, col 7 to line 614, col 50 of module drafts>
/\ tabs = ( t1 :> [worker |-> w2, status |-> "clean"] @@
  t2 :> [worker |-> w1, status |-> "clean"] )

State 8: <UserEditClean line 670, col 7 to line 672, col 59 of module drafts>
/\ tabs = ( t1 :> [worker |-> w2, status |-> "clean"] @@
  t2 :>
      [ worker |-> w1,
        status |-> "dirty",
        draft |-> [isEmpty |-> TRUE],
        localChange |-> TRUE ] )

State 9: <UserEditClean line 670, col 7 to line 672, col 59 of module drafts>
/\ tabs = ( t1 :>
      [ worker |-> w2,
        status |-> "dirty",
        draft |-> [isEmpty |-> TRUE],
        localChange |-> TRUE ] @@
  t2 :>
      [ worker |-> w1,
        status |-> "dirty",
        draft |-> [isEmpty |-> TRUE],
        localChange |-> TRUE ] )

State 10: <TabSendChangesToWorker line 652, col 7 to line 662, col 50 of module drafts>
/\ network = << [ drafts |-> {},
     type |-> "sync:T->W",
     from |-> t2,
     to |-> w1,
     newEdit |-> [isEmpty |-> FALSE, get |-> (w1 :> 0 @@ w2 :> 0)] ] >>
/\ tabs = ( t1 :>
      [ worker |-> w2,
        status |-> "dirty",
        draft |-> [isEmpty |-> TRUE],
        localChange |-> TRUE ] @@
  t2 :>
      [ worker |-> w1,
        status |-> "dirty",
        draft |-> [isEmpty |-> TRUE],
        localChange |-> FALSE ] )

State 11: <WorkerRecvChanges line 691, col 5 to line 718, col 49 of module drafts>
/\ network = << [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:W->T",
     from |-> w1,
     to |-> t2,
     newEdit |->
         [ isEmpty |-> FALSE,
           get |->
               [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] ] ] >>
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

State 12: <WorkerSendRemoteStoreCmd line 761, col 7 to line 766, col 47 of module drafts>
/\ network = << [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:W->T",
     from |-> w1,
     to |-> t2,
     newEdit |->
         [ isEmpty |-> FALSE,
           get |->
               [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] ] ],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "RemoteStoreCmd",
     from |-> w1,
     to |-> t2,
     id |-> 1 ] >>
/\ workers = ( w1 :>
      [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
        time |-> 2,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 1, lastReq |-> 1, lastAck |-> 0]] ] @@
  w2 :>
      [ drafts |-> {},
        time |-> 1,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 0, lastReq |-> 0, lastAck |-> 0]] ] )

State 13: <TabRecvRemoteStoreCmd line 619, col 5 to line 646, col 54 of module drafts>
/\ network = << [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:W->T",
     from |-> w1,
     to |-> t2,
     newEdit |->
         [ isEmpty |-> FALSE,
           get |->
               [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] ] ],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:T->R",
     from |-> t2,
     to |-> "Remote",
     id |-> 1 ] >>
/\ tabs = ( t1 :>
      [ worker |-> w2,
        status |-> "dirty",
        draft |-> [isEmpty |-> TRUE],
        localChange |-> TRUE ] @@
  t2 :>
      [ worker |-> w1,
        status |-> "dirty",
        draft |->
            [ isEmpty |-> FALSE,
              get |->
                  [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] ],
        localChange |-> FALSE ] )

State 14: <RemoteRecvDrafts line 480, col 5 to line 502, col 50 of module drafts>
/\ network = << [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:W->T",
     from |-> w1,
     to |-> t2,
     newEdit |->
         [ isEmpty |-> FALSE,
           get |->
               [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] ] ],
   [type |-> "ack:R->T", from |-> "Remote", to |-> t2, id |-> 1],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:R->T",
     from |-> "Remote",
     to |-> t1 ] >>
/\ remote = {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]}

State 15: <TabRecvRemoteAck line 771, col 5 to line 782, col 58 of module drafts>
/\ network = << [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:W->T",
     from |-> w1,
     to |-> t2,
     newEdit |->
         [ isEmpty |-> FALSE,
           get |->
               [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] ] ],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:R->T",
     from |-> "Remote",
     to |-> t1 ],
   [type |-> "ack:T->W", from |-> t2, to |-> w1, id |-> 1] >>

State 16: <WorkerRecvRemoteAck line 787, col 5 to line 794, col 49 of module drafts>
/\ network = << [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:W->T",
     from |-> w1,
     to |-> t2,
     newEdit |->
         [ isEmpty |-> FALSE,
           get |->
               [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] ] ],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:R->T",
     from |-> "Remote",
     to |-> t1 ] >>
/\ workers = ( w1 :>
      [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
        time |-> 2,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 1, lastReq |-> 1, lastAck |-> 1]] ] @@
  w2 :>
      [ drafts |-> {},
        time |-> 1,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 0, lastReq |-> 0, lastAck |-> 0]] ] )

State 17: <TabRecvDraftsFromRemote line 507, col 5 to line 529, col 52 of module drafts>
/\ network = << [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:W->T",
     from |-> w1,
     to |-> t2,
     newEdit |->
         [ isEmpty |-> FALSE,
           get |->
               [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] ] ],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:T->W",
     from |-> t1,
     to |-> w2,
     newEdit |-> [isEmpty |-> TRUE] ] >>
/\ tabs = ( t1 :>
      [ worker |-> w2,
        status |-> "dirty",
        draft |->
            [ isEmpty |-> FALSE,
              get |->
                  [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] ],
        localChange |-> TRUE ] @@
  t2 :>
      [ worker |-> w1,
        status |-> "dirty",
        draft |->
            [ isEmpty |-> FALSE,
              get |->
                  [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] ],
        localChange |-> FALSE ] )

State 18: <TabSendChangesToWorker line 652, col 7 to line 662, col 50 of module drafts>
/\ network = << [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:W->T",
     from |-> w1,
     to |-> t2,
     newEdit |->
         [ isEmpty |-> FALSE,
           get |->
               [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] ] ],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:T->W",
     from |-> t1,
     to |-> w2,
     newEdit |-> [isEmpty |-> TRUE] ],
   [ drafts |-> {},
     type |-> "sync:T->W",
     from |-> t1,
     to |-> w2,
     newEdit |-> [isEmpty |-> FALSE, get |-> (w1 :> 0 @@ w2 :> 0)] ] >>
/\ tabs = ( t1 :>
      [ worker |-> w2,
        status |-> "dirty",
        draft |->
            [ isEmpty |-> FALSE,
              get |->
                  [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] ],
        localChange |-> FALSE ] @@
  t2 :>
      [ worker |-> w1,
        status |-> "dirty",
        draft |->
            [ isEmpty |-> FALSE,
              get |->
                  [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] ],
        localChange |-> FALSE ] )

State 19: <WorkerRecvChanges line 691, col 5 to line 718, col 49 of module drafts>
/\ network = << [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:W->T",
     from |-> w1,
     to |-> t2,
     newEdit |->
         [ isEmpty |-> FALSE,
           get |->
               [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] ] ],
   [ drafts |-> {},
     type |-> "sync:T->W",
     from |-> t1,
     to |-> w2,
     newEdit |-> [isEmpty |-> FALSE, get |-> (w1 :> 0 @@ w2 :> 0)] ],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:W->T",
     from |-> w2,
     to |-> t1,
     newEdit |-> [isEmpty |-> TRUE] ] >>
/\ workers = ( w1 :>
      [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
        time |-> 2,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 1, lastReq |-> 1, lastAck |-> 1]] ] @@
  w2 :>
      [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
        time |-> 1,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 1, lastReq |-> 0, lastAck |-> 0]] ] )

State 20: <WorkerSendRemoteStoreCmd line 761, col 7 to line 766, col 47 of module drafts>
/\ network = << [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:W->T",
     from |-> w1,
     to |-> t2,
     newEdit |->
         [ isEmpty |-> FALSE,
           get |->
               [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] ] ],
   [ drafts |-> {},
     type |-> "sync:T->W",
     from |-> t1,
     to |-> w2,
     newEdit |-> [isEmpty |-> FALSE, get |-> (w1 :> 0 @@ w2 :> 0)] ],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:W->T",
     from |-> w2,
     to |-> t1,
     newEdit |-> [isEmpty |-> TRUE] ],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "RemoteStoreCmd",
     from |-> w2,
     to |-> t1,
     id |-> 1 ] >>
/\ workers = ( w1 :>
      [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
        time |-> 2,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 1, lastReq |-> 1, lastAck |-> 1]] ] @@
  w2 :>
      [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
        time |-> 1,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 1, lastReq |-> 1, lastAck |-> 0]] ] )

State 21: <TabRecvDraftsFromWorker line 534, col 5 to line 553, col 52 of module drafts>
/\ network = << [ drafts |-> {},
     type |-> "sync:T->W",
     from |-> t1,
     to |-> w2,
     newEdit |-> [isEmpty |-> FALSE, get |-> (w1 :> 0 @@ w2 :> 0)] ],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:W->T",
     from |-> w2,
     to |-> t1,
     newEdit |-> [isEmpty |-> TRUE] ],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "RemoteStoreCmd",
     from |-> w2,
     to |-> t1,
     id |-> 1 ] >>

State 22: <TabRecvDraftsFromWorker line 534, col 5 to line 553, col 52 of module drafts>
/\ network = << [ drafts |-> {},
     type |-> "sync:T->W",
     from |-> t1,
     to |-> w2,
     newEdit |-> [isEmpty |-> FALSE, get |-> (w1 :> 0 @@ w2 :> 0)] ],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "RemoteStoreCmd",
     from |-> w2,
     to |-> t1,
     id |-> 1 ] >>

State 23: <WorkerRecvChanges line 691, col 5 to line 718, col 49 of module drafts>
/\ network = << [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "RemoteStoreCmd",
     from |-> w2,
     to |-> t1,
     id |-> 1 ],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
     type |-> "sync:W->T",
     from |-> w2,
     to |-> t1,
     newEdit |->
         [ isEmpty |-> FALSE,
           get |->
               [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] ] ] >>
/\ workers = ( w1 :>
      [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
        time |-> 2,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 1, lastReq |-> 1, lastAck |-> 1]] ] @@
  w2 :>
      [ drafts |->
            { [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)],
              [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] },
        time |-> 2,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 2, lastReq |-> 1, lastAck |-> 0]] ] )

State 24: <TabRecvRemoteStoreCmd line 619, col 5 to line 646, col 54 of module drafts>
/\ network = << [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
     type |-> "sync:W->T",
     from |-> w2,
     to |-> t1,
     newEdit |->
         [ isEmpty |-> FALSE,
           get |->
               [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] ] ],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:T->R",
     from |-> t1,
     to |-> "Remote",
     id |-> 1 ] >>

State 25: <RemoteRecvDrafts line 480, col 5 to line 502, col 50 of module drafts>
/\ network = << [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
     type |-> "sync:W->T",
     from |-> w2,
     to |-> t1,
     newEdit |->
         [ isEmpty |-> FALSE,
           get |->
               [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] ] ],
   [type |-> "ack:R->T", from |-> "Remote", to |-> t1, id |-> 1],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:R->T",
     from |-> "Remote",
     to |-> t2 ] >>

State 26: <TabRecvRemoteAck line 771, col 5 to line 782, col 58 of module drafts>
/\ network = << [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
     type |-> "sync:W->T",
     from |-> w2,
     to |-> t1,
     newEdit |->
         [ isEmpty |-> FALSE,
           get |->
               [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] ] ],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:R->T",
     from |-> "Remote",
     to |-> t2 ],
   [type |-> "ack:T->W", from |-> t1, to |-> w2, id |-> 1] >>

State 27: <TabRecvDraftsFromWorker line 534, col 5 to line 553, col 52 of module drafts>
/\ network = << [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:R->T",
     from |-> "Remote",
     to |-> t2 ],
   [type |-> "ack:T->W", from |-> t1, to |-> w2, id |-> 1] >>
/\ tabs = ( t1 :>
      [ worker |-> w2,
        status |-> "dirty",
        draft |->
            [ isEmpty |-> FALSE,
              get |->
                  [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)] ],
        localChange |-> FALSE ] @@
  t2 :>
      [ worker |-> w1,
        status |-> "dirty",
        draft |->
            [ isEmpty |-> FALSE,
              get |->
                  [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] ],
        localChange |-> FALSE ] )

State 28: <TabRecvDraftsFromRemote line 507, col 5 to line 529, col 52 of module drafts>
/\ network = <<[type |-> "ack:T->W", from |-> t1, to |-> w2, id |-> 1]>>

State 29: <WorkerRecvRemoteAck line 787, col 5 to line 794, col 49 of module drafts>
/\ network = <<>>
/\ workers = ( w1 :>
      [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
        time |-> 2,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 1, lastReq |-> 1, lastAck |-> 1]] ] @@
  w2 :>
      [ drafts |->
            { [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)],
              [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] },
        time |-> 2,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 2, lastReq |-> 1, lastAck |-> 1]] ] )

State 30: <WorkerSendRemoteStoreCmd line 761, col 7 to line 766, col 47 of module drafts>
/\ network = << [ drafts |->
         { [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)],
           [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] },
     type |-> "RemoteStoreCmd",
     from |-> w2,
     to |-> t1,
     id |-> 2 ] >>
/\ workers = ( w1 :>
      [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
        time |-> 2,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 1, lastReq |-> 1, lastAck |-> 1]] ] @@
  w2 :>
      [ drafts |->
            { [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)],
              [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] },
        time |-> 2,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 2, lastReq |-> 2, lastAck |-> 1]] ] )

State 31: <TabRecvRemoteStoreCmd line 619, col 5 to line 646, col 54 of module drafts>
/\ network = << [ drafts |->
         { [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)],
           [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] },
     type |-> "sync:T->R",
     from |-> t1,
     to |-> "Remote",
     id |-> 2 ] >>
/\ tabs = ( t1 :>
      [ drafts |->
            { [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)],
              [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] },
        worker |-> w2,
        status |-> "conflicted",
        localChange |-> FALSE ] @@
  t2 :>
      [ worker |-> w1,
        status |-> "dirty",
        draft |->
            [ isEmpty |-> FALSE,
              get |->
                  [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] ],
        localChange |-> FALSE ] )

State 32: <RemoteRecvDrafts line 480, col 5 to line 502, col 50 of module drafts>
/\ network = << [type |-> "ack:R->T", from |-> "Remote", to |-> t1, id |-> 2],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
     type |-> "sync:R->T",
     from |-> "Remote",
     to |-> t2 ] >>
/\ remote = { [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)],
  [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] }

State 33: <TabRecvDraftsFromRemote line 507, col 5 to line 529, col 52 of module drafts>
/\ network = << [type |-> "ack:R->T", from |-> "Remote", to |-> t1, id |-> 2],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
     type |-> "sync:T->W",
     from |-> t2,
     to |-> w1,
     newEdit |-> [isEmpty |-> TRUE] ] >>
/\ tabs = ( t1 :>
      [ drafts |->
            { [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)],
              [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] },
        worker |-> w2,
        status |-> "conflicted",
        localChange |-> FALSE ] @@
  t2 :>
      [ worker |-> w1,
        status |-> "dirty",
        draft |->
            [ isEmpty |-> FALSE,
              get |->
                  [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)] ],
        localChange |-> FALSE ] )

State 34: <WorkerRecvChanges line 691, col 5 to line 718, col 49 of module drafts>
/\ network = << [type |-> "ack:R->T", from |-> "Remote", to |-> t1, id |-> 2],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
     type |-> "sync:W->T",
     from |-> w1,
     to |-> t2,
     newEdit |-> [isEmpty |-> TRUE] ] >>
/\ workers = ( w1 :>
      [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
        time |-> 2,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 2, lastReq |-> 1, lastAck |-> 1]] ] @@
  w2 :>
      [ drafts |->
            { [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)],
              [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] },
        time |-> 2,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 2, lastReq |-> 2, lastAck |-> 1]] ] )

State 35: <WorkerSendRemoteStoreCmd line 761, col 7 to line 766, col 47 of module drafts>
/\ network = << [type |-> "ack:R->T", from |-> "Remote", to |-> t1, id |-> 2],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
     type |-> "sync:W->T",
     from |-> w1,
     to |-> t2,
     newEdit |-> [isEmpty |-> TRUE] ],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
     type |-> "RemoteStoreCmd",
     from |-> w1,
     to |-> t2,
     id |-> 2 ] >>
/\ workers = ( w1 :>
      [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
        time |-> 2,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 2, lastReq |-> 2, lastAck |-> 1]] ] @@
  w2 :>
      [ drafts |->
            { [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)],
              [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] },
        time |-> 2,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 2, lastReq |-> 2, lastAck |-> 1]] ] )

State 36: <TabRecvDraftsFromWorker line 534, col 5 to line 553, col 52 of module drafts>
/\ network = << [type |-> "ack:R->T", from |-> "Remote", to |-> t1, id |-> 2],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
     type |-> "RemoteStoreCmd",
     from |-> w1,
     to |-> t2,
     id |-> 2 ] >>

State 37: <TabRecvRemoteStoreCmd line 619, col 5 to line 646, col 54 of module drafts>
/\ network = << [type |-> "ack:R->T", from |-> "Remote", to |-> t1, id |-> 2],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
     type |-> "sync:T->R",
     from |-> t2,
     to |-> "Remote",
     id |-> 2 ] >>

State 38: <TabRecvRemoteAck line 771, col 5 to line 782, col 58 of module drafts>
/\ network = << [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
     type |-> "sync:T->R",
     from |-> t2,
     to |-> "Remote",
     id |-> 2 ],
   [type |-> "ack:T->W", from |-> t1, to |-> w2, id |-> 2] >>

State 39: <RemoteRecvDrafts line 480, col 5 to line 502, col 50 of module drafts>
/\ network = << [type |-> "ack:T->W", from |-> t1, to |-> w2, id |-> 2],
   [type |-> "ack:R->T", from |-> "Remote", to |-> t2, id |-> 2],
   [ drafts |->
         { [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)],
           [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] },
     type |-> "sync:R->T",
     from |-> "Remote",
     to |-> t1 ] >>

State 40: <TabRecvRemoteAck line 771, col 5 to line 782, col 58 of module drafts>
/\ network = << [type |-> "ack:T->W", from |-> t1, to |-> w2, id |-> 2],
   [ drafts |->
         { [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)],
           [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] },
     type |-> "sync:R->T",
     from |-> "Remote",
     to |-> t1 ],
   [type |-> "ack:T->W", from |-> t2, to |-> w1, id |-> 2] >>

State 41: <WorkerRecvRemoteAck line 787, col 5 to line 794, col 49 of module drafts>
/\ network = << [ drafts |->
         { [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)],
           [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] },
     type |-> "sync:R->T",
     from |-> "Remote",
     to |-> t1 ],
   [type |-> "ack:T->W", from |-> t2, to |-> w1, id |-> 2] >>
/\ workers = ( w1 :>
      [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
        time |-> 2,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 2, lastReq |-> 2, lastAck |-> 1]] ] @@
  w2 :>
      [ drafts |->
            { [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)],
              [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] },
        time |-> 2,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 2, lastReq |-> 2, lastAck |-> 2]] ] )

State 42: <TabRecvDraftsFromRemote line 507, col 5 to line 529, col 52 of module drafts>
/\ network = <<[type |-> "ack:T->W", from |-> t2, to |-> w1, id |-> 2]>>
/\ tabs = ( t1 :>
      [ worker |-> w2,
        status |-> "dirty",
        draft |->
            [ isEmpty |-> FALSE,
              get |->
                  [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)] ],
        localChange |-> FALSE ] @@
  t2 :>
      [ worker |-> w1,
        status |-> "dirty",
        draft |->
            [ isEmpty |-> FALSE,
              get |->
                  [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)] ],
        localChange |-> FALSE ] )

State 43: <WorkerRecvRemoteAck line 787, col 5 to line 794, col 49 of module drafts>
/\ network = <<>>
/\ workers = ( w1 :>
      [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
        time |-> 2,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 2, lastReq |-> 2, lastAck |-> 2]] ] @@
  w2 :>
      [ drafts |->
            { [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)],
              [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] },
        time |-> 2,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 2, lastReq |-> 2, lastAck |-> 2]] ] )

The number of states generated: 133
Simulation using seed 0 and aril 0
Progress: 133 states checked.
Finished in 00s at (2020-12-04 12:56:38)
    """.stripMargin

}
