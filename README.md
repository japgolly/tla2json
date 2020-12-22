# tla2json: Convert TLA+ output to JSON

```
tla2json v1.0.0

Usage: tla2json [options] [<input file>]

  -D, --dump            Parse input as a dump
  -T, --trace           Parse input as a trace (default)
  -S, --state           Parse input as a state
  -V, --value           Parse input as a value
  -d, --diff            Convert a full trace into a diff-trace
  -f, --full            Convert a diff-trace back into a full trace
  -l, --full-last-step  Convert the last step of a diff-trace back into a full trace
  -i, --indent          Indent and pretty-print JSON output
  -s, --sort            Output the fields of each object with the keys in sorted order
  <input file>          File to parse
```

## Why/how is this useful?

The simplest and most common example is you checking your (non-trivial) model
and TLC finds a counter-example. Trying to make sense of a trace with many steps and/or
a large state space is immensely time-consuming and difficult. By converting the trace
to a widely-used format such as JSON, you can easily transform and/or query the data using
ubiquitous tools like [`jq`](https://stedolan.github.io/jq/), and get a sense of your data much faster.

A personal anecdote is me spending over 10 minutes trying to comprehend hundreds of lines of
TLA+ state, vs looking at a summary as a single-page table and comprehending the situation in under 10 seconds.

### Example 1: A concise summary

Instead of trying to read [something like this](https://gist.github.com/japgolly/19ee59f884bc13cefa9401b1f57c49cc),
using `tla2json`, `jq` and `column` can pretty easily turn it into a concise table like this:

```
> tla2json -s output.log | jq -rf summary.jq | column -t -s$'\t'

#   Step                       Target        Remote  Tabs                     Workers            Network
=   ====                       ======        ======  ====                     =======            =======
1   Initial predicate          []            []      {t1:-, t2:-}             {w1:-, w2:-}       []                                                          
2   TabNew                     -             -       {t1:[], t2:-}            {w1:[], w2:-}      -                                                           
3   TabLoad                    -             -       {t1:[], t2:-}            -                  -                                                           
4   TabNew                     -             -       {t1:[], t2:[]}           -                  -                                                           
5   TabLoad                    -             -       {t1:[], t2:[]}           -                  -                                                           
6   TabStart                   -             -       {t1:-, t2:[]}            -                  -                                                           
7   TabStart                   -             -       {t1:-, t2:-}             -                  -                                                           
8   UserEditClean              []→{t1:e1}    -       {t1:[], t2:-}            -                  -                                                           
9   UserAbort                  []→{t1:e1d}   -       {t1:[]A, t2:-}           -                  -                                                           
10  TabSendChangesToWorker     -             -       {t1:[]A, t2:-}           -                  [sync(t1→w1):[]+]                                           
11  WorkerRecvChanges          []←{t1:1.1d}  -       -                        {w1:[1.1], w2:-}   [sync(w1→t1):[1.1]+, sync(w1→t2):[1.1]]                     
12  TabRecvDraftsFromWorker    -             -       {t1:[]A, t2:[1.1]}       -                  [sync(w1→t1):[1.1]+]                                        
13  TabRecvDraftsFromWorker    [1.1d]        -       {t1:-[1.1d], t2:[1.1]}   -                  []                                                          
14  TabSendTombstonesToWorker  -             -       {t1:-, t2:[1.1]}         -                  [sync(t1→w1):[1.1d]]                                        
15  WorkerSendRemoteStoreCmd   -             -       -                        {w1:[1.1], w2:-}   [rcmd(w1→t2):[1.1], sync(t1→w1):[1.1d]]                     
16  TabRecvRemoteStoreCmd      -             -       -                        -                  [sync(t1→w1):[1.1d], sync(t2→R):[1.1]]                      
17  RemoteRecvDrafts           -             [1.1]   -                        -                  [sync(R→t1):[1.1], sync(t1→w1):[1.1d]]                      
18  WorkerRecvChanges          -             -       -                        {w1:[1.1d], w2:-}  [sync(R→t1):[1.1], sync(w1→t1):[1.1d], sync(w1→t2):[1.1d]]  
19  TabRecvDraftsFromWorker    -             -       {t1:-, t2:[1.1d]}        -                  [sync(R→t1):[1.1], sync(w1→t1):[1.1d]]                      
20  TabRecvDraftsFromWorker    -             -       {t1:-[1.1d], t2:[1.1d]}  -                  [sync(R→t1):[1.1]]                                          
21  TabRecvRemoteAck           -             -       -                        -                  [sync(R→t1):[1.1]]                                          
22  TabSendTombstonesToWorker  -             -       {t1:-, t2:[1.1d]}        -                  [sync(R→t1):[1.1], sync(t1→w1):[1.1d]]                      
23  WorkerRecvChanges          -             -       -                        -                  [sync(R→t1):[1.1]]                                          
24  WorkerRecvRemoteAck        -             -       -                        {w1:[1.1d], w2:-}  [sync(R→t1):[1.1]]                                          
25  WorkerSendRemoteStoreCmd   -             -       -                        {w1:[1.1d], w2:-}  [rcmd(w1→t1):[1.1d], sync(R→t1):[1.1]]                      
26  TabRecvRemoteStoreCmd      -             -       -                        -                  [sync(R→t1):[1.1], sync(t1→R):[1.1d]]                       
27  RemoteRecvDrafts           -             [1.1d]  -                        -                  [sync(R→t1):[1.1], sync(R→t2):[1.1d]]                       
28  TabRecvDraftsFromRemote    -             -       {t1:[1.1], t2:[1.1d]}    -                  [sync(R→t2):[1.1d], sync(t1→w1):[1.1]]                      
29  TabRecvDraftsFromRemote    -             -       -                        -                  [sync(t1→w1):[1.1], sync(t2→w1):[1.1d]]                     
30  TabRecvRemoteAck           -             -       -                        -                  [sync(t1→w1):[1.1], sync(t2→w1):[1.1d]]                     
31  WorkerRecvChanges          -             -       -                        {w1:[1.1d], w2:-}  [sync(t2→w1):[1.1d]]                                        
32  WorkerRecvChanges          -             -       -                        -                  […]                                                         
33  WorkerRecvRemoteAck        -             -       -                        {w1:[], w2:-}      []                                                          
34  WorkerSendRemoteStoreCmd   [1.1d]        [1.1d]  {t1:[1.1], t2:[1.1d]}    {w1:[], w2:-}      []                                                          
```

*(It doesn't look great in this width-contrained README but it's made for my wider terminal.)*

Obviously this is only concise and quickly-comprehensible to me because I understand the domain of my spec.
You have to write your own `jq` transformations that make sense for your spec.
There's no one-size-fits-all way to summarise your state, but now you have the tools.

### Example 2: Querying your data

Taking the above example, let's say I'm only interested in knowing how the `.tabs[*].status`
values change. No problem! I'll write a new `jq` transformation on the fly to extract this data.

I'll even pass a `-f` flag to `tla2json` to convert my diff-trace into a full trace (so that
even if the `tabs` state didn't change on a step, it still shows me what the state is).

```
> tla2json -f <output.log | jq -c '.[] | {no, tabs: [.state.tabs[].status]}'

{"no":1,"tabs":["-","-"]}
{"no":2,"tabs":["loading","-"]}
{"no":3,"tabs":["loading","-"]}
{"no":4,"tabs":["loading","loading"]}
{"no":5,"tabs":["loading","loading"]}
{"no":6,"tabs":["clean","loading"]}
{"no":7,"tabs":["clean","clean"]}
{"no":8,"tabs":["dirty","clean"]}
{"no":9,"tabs":["dirty","clean"]}
{"no":10,"tabs":["dirty","clean"]}
{"no":11,"tabs":["dirty","clean"]}
{"no":12,"tabs":["dirty","dirty"]}
{"no":13,"tabs":["clean","dirty"]}
{"no":14,"tabs":["clean","dirty"]}
{"no":15,"tabs":["clean","dirty"]}
{"no":16,"tabs":["clean","dirty"]}
{"no":17,"tabs":["clean","dirty"]}
{"no":18,"tabs":["clean","dirty"]}
{"no":19,"tabs":["clean","dirty"]}
{"no":20,"tabs":["clean","dirty"]}
{"no":21,"tabs":["clean","dirty"]}
{"no":22,"tabs":["clean","dirty"]}
{"no":23,"tabs":["clean","dirty"]}
{"no":24,"tabs":["clean","dirty"]}
{"no":25,"tabs":["clean","dirty"]}
{"no":26,"tabs":["clean","dirty"]}
{"no":27,"tabs":["clean","dirty"]}
{"no":28,"tabs":["dirty","dirty"]}
{"no":29,"tabs":["dirty","dirty"]}
{"no":30,"tabs":["dirty","dirty"]}
{"no":31,"tabs":["dirty","dirty"]}
{"no":32,"tabs":["dirty","dirty"]}
{"no":33,"tabs":["dirty","dirty"]}
{"no":34,"tabs":["dirty","dirty"]}
```

## Installation

* Arch Linux: `tla2json` is in the [AUR](https://aur.archlinux.org/packages/tla2json/)

    ```sh
    yay -S tla2json
    tla2json --help
    ```

* Everything else:

    ```sh
    wget https://github.com/japgolly/tla2json/releases/download/v1.0.0/tla2json.jar
    java -jar tla2json.jar --help
    ```


## Support

If you like what I do
—my OSS libraries, my contributions to other OSS libs, [my programming blog](https://japgolly.blogspot.com)—
and you'd like to support me, more content, more lib maintenance, [please become a patron](https://www.patreon.com/japgolly)!
I do all my OSS work unpaid so showing your support will make a big difference.
