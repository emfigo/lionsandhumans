(ns lionsandhumans.initiate
  (:require [lionsandhumans 
             [db :as db]
             [algorithm :as alg]
             [config :as conf]]
            [langohr
             [basic :as lb]
             [channel :as lch]
             [consumers :as lc]
             [core :as rmq]
             [queue :as lq]]))

(defn start
  [&args]
  (let
    [list-of-objects (db/db-read "shoreA")
     decisions-stream (alg/compute-decisions list-of-objects)]
    (process-stream decisions-stream)))
