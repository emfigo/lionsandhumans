(ns lionsandhumans.send-boat
  (:require [langohr
             [basic :as lb]
             [channel :as lch]
             [consumers :as lc]
            [core :as rmq]
             [queue :as lq]]
            [core.data.json :as json]
            [lionsandhumans
             [docking :as dc]]))


(defn human-inside?
  [boat]
  (not (and 
        (nil? (re-matches #"H\d+" (:seatA boat))) 
        (nil? (re-matches #"H\d+" (:seatB boat))))))


(defn sail-out
 [boat con]
 (let
   [msg (json/write-str boat)
    exchange (-> conf/params :rabbit :exchange)
    routing-key (-> conf/params :rabbit :routing-key)]
   (lb/publish con 
               exchange 
               routing-key 
               msg 
               {:content-type "application/json"})))


(defn get-on-board
  [x y]
  {:seatA (if (nil? x) "empty" x) 
   :seatB (if (nil? y) "empty" y)})


(defn send-the-boat-to-A
  [x y con]
  (let
    [boat (get-on-board x y)]
    (if (and (nil? (:seatA boat)) (nil? (:seatB boat))) 
      (println "Boat is empty. Unable to send the boat to shore A---" boat)
      (if (human-inside?)
        (do 
         (sail-out boat)
         (dc/dock-in "A"))
        (println 
         "There is no human to operate the boat and so it stays in shore B")))))


(defn send-the-boat-to-B
  [x y con]
  (let
    [boat (get-on-board x y)]
    (if (and (nil? (:seatA boat)) (nil? (:seatB boat))) 
      (println "Boat is empty. Unable to send the boat to shore B---" boat)
      (if (human-inside?)
        (do 
         (sail-out boat con)
         (dc/dock-in "B"))
        (println 
         "There is no human to operate the boat and so it stays in shore A")))))


