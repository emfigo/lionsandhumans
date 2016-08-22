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
  [objs]
  (let
    [decisions-stream (alg/compute-decisions objs)]
    (apply #(db/write shoreA %) objs )
    (process-stream decisions-stream)))
