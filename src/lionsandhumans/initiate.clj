(ns lionsandhumans.initiate
  (:gen-class)
  (:require [lionsandhumans 
             [db :as db]
             [boat-sender :as bs]
             [docking :as dc]
             [algorithm :as alg]
             [config :as conf]]
            [langohr
             [basic :as lb]
             [channel :as lch]
             [consumers :as lc]
             [core :as rmq]
             [queue :as lq]]))

(defn- process
  [ch coll]
  (let
    [triple (first coll)
     pA (:passengerA triple)
     pB (:passengerB triple)
     destination-shore (:shore triple)
     starting-shore (case destination-shore
                      "shoreA" "shoreB"
                      "shoreB" "shoreA")]
     (bs/send-boat pA pB destination-shore ch)
     (if (empty? (rest coll))
       (println "######### END OF STREAM #########")
       (process ch (rest coll))))) ;;CHANGEME


(defn process-stream
  [ch stream]
  (process ch stream))

(defn start
  [objs]
  (let
    [decisions-stream (alg/compute-decisions objs)
     conn (rmq/connect {:uri (-> conf/params :rabbit :uri)})
     ch (lch/open conn)]
    (db/db-write "shoreA" objs)
    (dc/dock-in ch "shoreA")
    (dc/dock-in ch "shoreB")
    (process-stream ch decisions-stream)))
