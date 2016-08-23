(ns lionsandhumans.config
  (:gen-class))

(def params
  {:rabbit {:uri "amqp://guest:guest@192.168.99.100:5672"
            :exchange "standard"
            :queue {"shoreA" "coolest_transport_to_shore_a"
                    "shoreB" "coolest_transport_to_shore_b"}
            :routing-key {"shoreA" "paradise.boat.shorea"
                          "shoreB" "paradise.boat.shoreb"}}
   :redis {:server-conn {:pool {} 
                         :spec {:host "127.0.0.1" :port 6379}}
           :db-key {"shoreA" "shorea"
                    "shoreB" "shoreb"}}})
