(ns lionsandhumans.config)

(def params
  {:rabbit {:uri "amqp://guest:guest@192.168.99.100:5672"
            :exchange "standard"
            :queue "coolest_transport"
            :routing-key "paradise.boat"}})
