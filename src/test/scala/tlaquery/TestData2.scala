package tlaquery

object TestData2Test extends TestData.PropTest {
  override val testData = TestData2
  override def firstPostStateLine = "4162900 states generated"
}

object TestData2 extends TestData {

  val output =
    """
TLC2 Version 2.15 of Day Month 20?? (rev: eb3ff99)
Running breadth-first search Model-Checking with fp 8 and seed -5048094833486122706 with 24 workers on 24 cores with 25486MB heap and 64MB offheap memory [pid: 953375] (Linux 5.9.11-arch2-1 amd64, GraalVM Community 11.0.8 x86_64, MSBDiskFPSet, DiskStateQueue).
Parsing file /tmp/20201205-205655-953367/drafts.tla
Parsing file /tmp/FiniteSets.tla
Parsing file /tmp/Naturals.tla
Parsing file /tmp/Sequences.tla
Parsing file /tmp/TLC.tla
Parsing file /tmp/20201205-205655-953367/Util.tla
Semantic processing of module Naturals
Semantic processing of module Sequences
Semantic processing of module FiniteSets
Semantic processing of module TLC
Semantic processing of module Util
Semantic processing of module drafts
Starting... (2020-12-05 20:56:55)
"PruneTest PASS."
Computing initial states...
Finished computing initial states: 1 distinct state generated at 2020-12-05 20:56:55.
Progress(25) at 2020-12-05 20:56:59: 368,049 states generated (368,049 s/min), 123,053 distinct states found (123,053 ds/min), 54,763 states left on queue.
"Error: Drafts are not eventually-consistent"
[ AB |-> {},
  AW |-> {w1, w2},
  AT |-> {t1, t2},
  Stores |->
      { {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
        { [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)],
          [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] } } ]
Error: Invariant StableInvariants is violated.
Error: The behavior up to this point is:
State 1: <Initial predicate>
/\ browsers = (b1 :> << >>)
/\ network = <<>>
/\ tabs = (t1 :> [status |-> "-"] @@ t2 :> [status |-> "-"])
/\ remote = {}
/\ workers = (w1 :> [status |-> "-"] @@ w2 :> [status |-> "-"])

State 2: <TabNew line 622, col 5 to line 636, col 10 of module drafts>
/\ tabs = ( t1 :>
      [ drafts |-> {},
        worker |-> w1,
        status |-> "loading",
        awaiting |-> {"Remote"} ] @@
  t2 :> [status |-> "-"] )
/\ workers = ( w1 :>
      [ drafts |-> {},
        time |-> 1,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 0, lastReq |-> 0, lastAck |-> 0]] ] @@
  w2 :> [status |-> "-"] )

State 3: <TabLoad line 598, col 5 to line 618, col 67 of module drafts>
/\ tabs = ( t1 :> [drafts |-> {}, worker |-> w1, status |-> "loading", awaiting |-> {}] @@
  t2 :> [status |-> "-"] )

State 4: <TabNew line 622, col 5 to line 636, col 10 of module drafts>
/\ tabs = ( t1 :> [drafts |-> {}, worker |-> w1, status |-> "loading", awaiting |-> {}] @@
  t2 :>
      [ drafts |-> {},
        worker |-> w2,
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

State 5: <TabLoad line 598, col 5 to line 618, col 67 of module drafts>
/\ tabs = ( t1 :> [drafts |-> {}, worker |-> w1, status |-> "loading", awaiting |-> {}] @@
  t2 :> [drafts |-> {}, worker |-> w2, status |-> "loading", awaiting |-> {}] )

State 6: <TabStart line 643, col 7 to line 655, col 50 of module drafts>
/\ tabs = ( t1 :> [worker |-> w1, status |-> "clean"] @@
  t2 :> [drafts |-> {}, worker |-> w2, status |-> "loading", awaiting |-> {}] )

State 7: <TabStart line 643, col 7 to line 655, col 50 of module drafts>
/\ tabs = ( t1 :> [worker |-> w1, status |-> "clean"] @@
  t2 :> [worker |-> w2, status |-> "clean"] )

State 8: <UserEditClean line 711, col 7 to line 713, col 59 of module drafts>
/\ tabs = ( t1 :>
      [ worker |-> w1,
        status |-> "dirty",
        draft |-> [isEmpty |-> TRUE],
        localChange |-> TRUE ] @@
  t2 :> [worker |-> w2, status |-> "clean"] )

State 9: <TabSendChangesToWorker line 693, col 7 to line 703, col 50 of module drafts>
/\ network = << [ drafts |-> {},
     type |-> "sync:T->W",
     from |-> t1,
     to |-> w1,
     newEdit |-> [isEmpty |-> FALSE, get |-> (w1 :> 0 @@ w2 :> 0)] ] >>
/\ tabs = ( t1 :>
      [ worker |-> w1,
        status |-> "dirty",
        draft |-> [isEmpty |-> TRUE],
        localChange |-> FALSE ] @@
  t2 :> [worker |-> w2, status |-> "clean"] )

State 10: <UserEditClean line 711, col 7 to line 713, col 59 of module drafts>
/\ tabs = ( t1 :>
      [ worker |-> w1,
        status |-> "dirty",
        draft |-> [isEmpty |-> TRUE],
        localChange |-> FALSE ] @@
  t2 :>
      [ worker |-> w2,
        status |-> "dirty",
        draft |-> [isEmpty |-> TRUE],
        localChange |-> TRUE ] )

State 11: <TabSendChangesToWorker line 693, col 7 to line 703, col 50 of module drafts>
/\ network = << [ drafts |-> {},
     type |-> "sync:T->W",
     from |-> t1,
     to |-> w1,
     newEdit |-> [isEmpty |-> FALSE, get |-> (w1 :> 0 @@ w2 :> 0)] ],
   [ drafts |-> {},
     type |-> "sync:T->W",
     from |-> t2,
     to |-> w2,
     newEdit |-> [isEmpty |-> FALSE, get |-> (w1 :> 0 @@ w2 :> 0)] ] >>
/\ tabs = ( t1 :>
      [ worker |-> w1,
        status |-> "dirty",
        draft |-> [isEmpty |-> TRUE],
        localChange |-> FALSE ] @@
  t2 :>
      [ worker |-> w2,
        status |-> "dirty",
        draft |-> [isEmpty |-> TRUE],
        localChange |-> FALSE ] )

State 12: <WorkerRecvChanges line 732, col 5 to line 759, col 49 of module drafts>
/\ network = << [ drafts |-> {},
     type |-> "sync:T->W",
     from |-> t2,
     to |-> w2,
     newEdit |-> [isEmpty |-> FALSE, get |-> (w1 :> 0 @@ w2 :> 0)] ],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:W->T",
     from |-> w1,
     to |-> t1,
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

State 13: <TabRecvDraftsFromWorker line 575, col 5 to line 594, col 52 of module drafts>
/\ network = << [ drafts |-> {},
     type |-> "sync:T->W",
     from |-> t2,
     to |-> w2,
     newEdit |-> [isEmpty |-> FALSE, get |-> (w1 :> 0 @@ w2 :> 0)] ] >>
/\ tabs = ( t1 :>
      [ worker |-> w1,
        status |-> "dirty",
        draft |->
            [ isEmpty |-> FALSE,
              get |->
                  [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] ],
        localChange |-> FALSE ] @@
  t2 :>
      [ worker |-> w2,
        status |-> "dirty",
        draft |-> [isEmpty |-> TRUE],
        localChange |-> FALSE ] )

State 14: <WorkerRecvChanges line 732, col 5 to line 759, col 49 of module drafts>
/\ network = << [ drafts |-> {[worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:W->T",
     from |-> w2,
     to |-> t2,
     newEdit |->
         [ isEmpty |-> FALSE,
           get |->
               [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] ] ] >>
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

State 15: <TabRecvDraftsFromWorker line 575, col 5 to line 594, col 52 of module drafts>
/\ network = <<>>
/\ tabs = ( t1 :>
      [ worker |-> w1,
        status |-> "dirty",
        draft |->
            [ isEmpty |-> FALSE,
              get |->
                  [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] ],
        localChange |-> FALSE ] @@
  t2 :>
      [ worker |-> w2,
        status |-> "dirty",
        draft |->
            [ isEmpty |-> FALSE,
              get |->
                  [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] ],
        localChange |-> FALSE ] )

State 16: <WorkerSendRemoteStoreCmd line 802, col 7 to line 807, col 47 of module drafts>
/\ network = << [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "RemoteStoreCmd",
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

State 17: <TabRecvRemoteStoreCmd line 660, col 5 to line 687, col 54 of module drafts>
/\ network = << [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:T->R",
     from |-> t1,
     to |-> "Remote",
     id |-> 1 ] >>

State 18: <RemoteRecvDrafts line 520, col 5 to line 543, col 50 of module drafts>
/\ network = << [type |-> "ack:R->T", from |-> "Remote", to |-> t1, id |-> 1],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
     type |-> "sync:R->T",
     from |-> "Remote",
     to |-> t2 ] >>
/\ remote = {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]}

State 19: <TabRecvDraftsFromRemote line 548, col 5 to line 570, col 52 of module drafts>
/\ network = << [type |-> "ack:R->T", from |-> "Remote", to |-> t1, id |-> 1],
   [ drafts |->
         { [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)],
           [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] },
     type |-> "sync:T->W",
     from |-> t2,
     to |-> w2,
     newEdit |-> [isEmpty |-> TRUE] ] >>
/\ tabs = ( t1 :>
      [ worker |-> w1,
        status |-> "dirty",
        draft |->
            [ isEmpty |-> FALSE,
              get |->
                  [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] ],
        localChange |-> FALSE ] @@
  t2 :>
      [ drafts |->
            { [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)],
              [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] },
        worker |-> w2,
        status |-> "conflicted",
        localChange |-> FALSE ] )

State 20: <TabRecvRemoteAck line 812, col 5 to line 823, col 58 of module drafts>
/\ network = << [ drafts |->
         { [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)],
           [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] },
     type |-> "sync:T->W",
     from |-> t2,
     to |-> w2,
     newEdit |-> [isEmpty |-> TRUE] ],
   [type |-> "ack:T->W", from |-> t1, to |-> w1, id |-> 1] >>

State 21: <WorkerRecvChanges line 732, col 5 to line 759, col 49 of module drafts>
/\ network = << [type |-> "ack:T->W", from |-> t1, to |-> w1, id |-> 1],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
     type |-> "sync:W->T",
     from |-> w2,
     to |-> t2,
     newEdit |-> [isEmpty |-> TRUE] ] >>
/\ workers = ( w1 :>
      [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)]},
        time |-> 2,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 1, lastReq |-> 1, lastAck |-> 0]] ] @@
  w2 :>
      [ drafts |->
            { [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)],
              [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] },
        time |-> 2,
        status |-> "live",
        browser |-> b1,
        sync |-> [Remote |-> [desired |-> 1, lastReq |-> 0, lastAck |-> 0]] ] )

State 22: <TabRecvDraftsFromWorker line 575, col 5 to line 594, col 52 of module drafts>
/\ network = <<[type |-> "ack:T->W", from |-> t1, to |-> w1, id |-> 1]>>
/\ tabs = ( t1 :>
      [ worker |-> w1,
        status |-> "dirty",
        draft |->
            [ isEmpty |-> FALSE,
              get |->
                  [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] ],
        localChange |-> FALSE ] @@
  t2 :>
      [ worker |-> w2,
        status |-> "dirty",
        draft |->
            [ isEmpty |-> FALSE,
              get |->
                  [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)] ],
        localChange |-> FALSE ] )

State 23: <WorkerRecvRemoteAck line 828, col 5 to line 835, col 49 of module drafts>
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
        sync |-> [Remote |-> [desired |-> 1, lastReq |-> 0, lastAck |-> 0]] ] )

State 24: <WorkerSendRemoteStoreCmd line 802, col 7 to line 807, col 47 of module drafts>
/\ network = << [ drafts |->
         { [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)],
           [worker |-> w2, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 0)] },
     type |-> "RemoteStoreCmd",
     from |-> w2,
     to |-> t2,
     id |-> 1 ] >>
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
        sync |-> [Remote |-> [desired |-> 1, lastReq |-> 1, lastAck |-> 0]] ] )

State 25: <TabRecvRemoteStoreCmd line 660, col 5 to line 687, col 54 of module drafts>
/\ network = << [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
     type |-> "sync:T->R",
     from |-> t2,
     to |-> "Remote",
     id |-> 1 ] >>

State 26: <RemoteRecvDrafts line 520, col 5 to line 543, col 50 of module drafts>
/\ network = << [type |-> "ack:R->T", from |-> "Remote", to |-> t2, id |-> 1],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
     type |-> "sync:R->T",
     from |-> "Remote",
     to |-> t1 ] >>
/\ remote = {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]}

State 27: <TabRecvDraftsFromRemote line 548, col 5 to line 570, col 52 of module drafts>
/\ network = << [type |-> "ack:R->T", from |-> "Remote", to |-> t2, id |-> 1],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
     type |-> "sync:T->W",
     from |-> t1,
     to |-> w1,
     newEdit |-> [isEmpty |-> TRUE] ] >>
/\ tabs = ( t1 :>
      [ worker |-> w1,
        status |-> "dirty",
        draft |->
            [ isEmpty |-> FALSE,
              get |->
                  [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)] ],
        localChange |-> FALSE ] @@
  t2 :>
      [ worker |-> w2,
        status |-> "dirty",
        draft |->
            [ isEmpty |-> FALSE,
              get |->
                  [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)] ],
        localChange |-> FALSE ] )

State 28: <TabRecvRemoteAck line 812, col 5 to line 823, col 58 of module drafts>
/\ network = << [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
     type |-> "sync:T->W",
     from |-> t1,
     to |-> w1,
     newEdit |-> [isEmpty |-> TRUE] ],
   [type |-> "ack:T->W", from |-> t2, to |-> w2, id |-> 1] >>

State 29: <WorkerRecvChanges line 732, col 5 to line 759, col 49 of module drafts>
/\ network = << [type |-> "ack:T->W", from |-> t2, to |-> w2, id |-> 1],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
     type |-> "sync:W->T",
     from |-> w1,
     to |-> t1,
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
        sync |-> [Remote |-> [desired |-> 1, lastReq |-> 1, lastAck |-> 0]] ] )

State 30: <TabRecvDraftsFromWorker line 575, col 5 to line 594, col 52 of module drafts>
/\ network = <<[type |-> "ack:T->W", from |-> t2, to |-> w2, id |-> 1]>>

State 31: <WorkerSendRemoteStoreCmd line 802, col 7 to line 807, col 47 of module drafts>
/\ network = << [type |-> "ack:T->W", from |-> t2, to |-> w2, id |-> 1],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
     type |-> "RemoteStoreCmd",
     from |-> w1,
     to |-> t1,
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
        sync |-> [Remote |-> [desired |-> 1, lastReq |-> 1, lastAck |-> 0]] ] )

State 32: <TabRecvRemoteStoreCmd line 660, col 5 to line 687, col 54 of module drafts>
/\ network = << [type |-> "ack:T->W", from |-> t2, to |-> w2, id |-> 1],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
     type |-> "sync:T->R",
     from |-> t1,
     to |-> "Remote",
     id |-> 2 ] >>

State 33: <RemoteRecvDrafts line 520, col 5 to line 543, col 50 of module drafts>
/\ network = << [type |-> "ack:T->W", from |-> t2, to |-> w2, id |-> 1],
   [type |-> "ack:R->T", from |-> "Remote", to |-> t1, id |-> 2],
   [ drafts |-> {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]},
     type |-> "sync:R->T",
     from |-> "Remote",
     to |-> t2 ] >>

State 34: <TabRecvDraftsFromRemote line 548, col 5 to line 570, col 52 of module drafts>
/\ network = << [type |-> "ack:T->W", from |-> t2, to |-> w2, id |-> 1],
   [type |-> "ack:R->T", from |-> "Remote", to |-> t1, id |-> 2] >>

State 35: <TabRecvRemoteAck line 812, col 5 to line 823, col 58 of module drafts>
/\ network = << [type |-> "ack:T->W", from |-> t2, to |-> w2, id |-> 1],
   [type |-> "ack:T->W", from |-> t1, to |-> w1, id |-> 2] >>

State 36: <WorkerRecvRemoteAck line 828, col 5 to line 835, col 49 of module drafts>
/\ network = <<[type |-> "ack:T->W", from |-> t1, to |-> w1, id |-> 2]>>
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
        sync |-> [Remote |-> [desired |-> 1, lastReq |-> 1, lastAck |-> 1]] ] )

State 37: <WorkerRecvRemoteAck line 828, col 5 to line 835, col 49 of module drafts>
/\ browsers = (b1 :> << >>)
/\ network = <<>>
/\ tabs = ( t1 :>
      [ worker |-> w1,
        status |-> "dirty",
        draft |->
            [ isEmpty |-> FALSE,
              get |->
                  [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)] ],
        localChange |-> FALSE ] @@
  t2 :>
      [ worker |-> w2,
        status |-> "dirty",
        draft |->
            [ isEmpty |-> FALSE,
              get |->
                  [worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)] ],
        localChange |-> FALSE ] )
/\ remote = {[worker |-> w1, time |-> 1, prov |-> (w1 :> 0 @@ w2 :> 1)]}
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
        sync |-> [Remote |-> [desired |-> 1, lastReq |-> 1, lastAck |-> 1]] ] )

4162900 states generated, 1098292 distinct states found, 150005 states left on queue.
The depth of the complete state graph search is 37.
The average outdegree of the complete state graph is 1 (minimum is 0, the maximum 11 and the 95th percentile is 4).
Finished in 18s at (2020-12-05 20:57:14)
    """.stripMargin

}
