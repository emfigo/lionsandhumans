(ns lionsandhumans.docking)

(defn dock-in
  [shore]
  (consume-msg ch shore shore)
  (println "I write consumed data to database"))

;;
(defn consume-msg
  [ch shore queue-name]
  (let
    [queue' (lq/declare ch queue {:exclusive false :auto-delete true})
     handler handle-msg]
    (lq/bind lq/bindch queue-name' weather-exchange {:routing-key topic-name})
    (lc/subscribe ch queue-name' handler {:auto-ack true})))

;;store to db
(defn handle-msg 
  [ch {:keys [routing-key] :as meta} ^bytes payload]
  (println (format "[consumer] Consumed '%s' from %s, routing key: %s" (String. payload "UTF-8") queue-name' routing-key))) 
