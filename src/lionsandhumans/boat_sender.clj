(ns lionsandhumans.boat-sender
  (:gen-class)
  (:require [langohr
             [basic :as lb]
             [channel :as lch]
             [consumers :as lc]
             [core :as rmq]
             [queue :as lq]]
            [lionsandhumans
             [config :as conf]]
            [clojure.data.json :as json]))


(defn human-inside?
  [boat]
  (not (and 
        (nil? (re-matches #"H\d+" (:seatA boat))) 
        (nil? (re-matches #"H\d+" (:seatB boat))))))


(defn sail-out
 [boat shore ch]
 (let
   [msg (json/write-str boat)
    exchange (-> conf/params :rabbit :exchange)
    routing-key (get (-> conf/params :rabbit :routing-key) shore)]
   (lb/publish ch 
               exchange 
               routing-key 
               msg 
               {:content-type "application/json"})))


(defn get-on-board
  [x y]
  {:seatA (if (nil? x) "empty" x) 
   :seatB (if (nil? y) "empty" y)})


(defn send-boat
  [x y shore ch]
  (let
    [boat (get-on-board x y)]
    (if (and (nil? (:seatA boat)) 
             (nil? (:seatB boat))) 
      (println 
       "Boat is empty. Unable to send the boat to shore" 
       shore 
       "---" 
       boat)
      (if (human-inside? boat)
        (sail-out boat shore ch)
        (println 
         "There is no human to operate the boat and so it stays in shore "
         shore)))))
