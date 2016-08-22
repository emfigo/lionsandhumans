(ns lionsandhumans.process)

(defn- process
  [triple]
  (let
    [conn (rmq/connect {:uri (-> conf/params :rabbit :uri)})
     ch (lch/open conn)
     pA (:passengerA triple)
     pB (:passengerB triple)
     destination-shore (:shore triple)
     starting-shore (case destination-shore
                      "shoreA" "shoreB"
                      "shoreB" "shoreA")]
    (do
     (db/del starting-shore pA )
     (db/del starting-shore pB )
     (send-boat pA pB destination-shore ch)
     (dc/dock-in ch destination-shore))))

(defn process-stream
  [stream]
  (apply process stream))
