(ns lionsandhumans.docking
  (:gen-class)
  (:require [lionsandhumans
             [config :as conf]
             [db :as db]]
            [langohr
             [basic :as lb]
             [channel :as lch]
             [consumers :as lc]
             [core :as rmq]
             [queue :as lq]]
            [clojure.data.json :as json]))

(defn consume-msg
  [ch destination-shore]
  (let [queue-name (get (-> conf/params :rabbit :queue) destination-shore)
        db-key (get (-> conf/params :redis :db-key) destination-shore)
        msg-handler (fn 
                      [ch metadata ^bytes payload]
                      ;                      (println (format "[consumer] %s received a message: %s"
                      ;                                       queue-name
                      ;                                       (String. payload "UTF-8")))
                      (let
                        [message (json/read-str (String. payload "UTF-8"))
                         passengerA (get message "seatA")
                         passengerB (get message "seatB")
                         source-shore (case destination-shore
                                        "shoreA" "shoreB"
                                        "shoreB" "shoreA")
                         old-destination (db/db-read destination-shore)
                         old-source (db/db-read source-shore)
                         new-source (remove 
                                     #{passengerA passengerB} 
                                     old-source)
                         new-destination (apply conj old-destination
                                                (filter #(not= "empty" %) 
                                                        [passengerA passengerB]))
                         ]
                        (println "oldsrc " old-source 
                                 " olddest " old-destination 
                                 " message " (class message) 
                                 " passengerA " passengerA 
                                 " passengerB " passengerB 
                                 " destination-shore " destination-shore 
                                 " source-shore " source-shore 
                                 " payload " payload)
                        (db/db-write destination-shore new-destination)
                        (db/db-write source-shore new-source)
                        ))

        thread  (Thread. (fn [] 
                           (lc/subscribe 
                            ch 
                            queue-name 
                            msg-handler 
                            {:auto-ack true})))]
    (.start thread)
    ))

(defn dock-in
  [ch shore]
  (consume-msg ch shore))
