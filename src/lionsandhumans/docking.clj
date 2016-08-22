(ns lionsandhumans.docking)

(defn dock-in
  [shore]
  (consume-msg ch shore shore)
  (println "I write consumed data to database"))

;;
(defn consume-msg
  [ch shore]
  (let
    [queue-name (-> conf/params :rabbit :queue)
     consumer  (lc/create-default ch
                               {:handle-delivery-fn handle-msg 
                                :handle-consume-ok-fn      (or (get cons-opts :handle-consume-ok-fn)
                                                               (get cons-opts :handle-consume-ok))
                                :handle-cancel-ok-fn       (or (get cons-opts :handle-cancel-ok-fn)
                                                               (get cons-opts :handle-cancel-ok))
                                :handle-cancel-fn          (or (get cons-opts :handle-cancel-fn)
                                                               (get cons-opts :handle-cancel))
                                :handle-recover-ok-fn      (or (get cons-opts :handle-recover-ok-fn)
                                                               (get cons-opts :handle-recover-ok))
                                :handle-shutdown-signal-fn (or (get cons-opts :handle-shutdown-signal-fn)
                                                               (get cons-opts :handle-shutdown-signal))})]
    (lb/consume ch queue-name consumer {})
    ()))

;;store to db
(defn handle-msg 
  [ch {:keys [routing-key] :as meta} ^bytes payload]
  (println (format "[consumer] Consumed '%s' from %s, routing key: %s" 
                   (String. payload "UTF-8") 
                   "coolest_transport" 
                   routing-key))) 
