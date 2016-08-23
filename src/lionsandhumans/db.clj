(ns lionsandhumans.db
  (:gen-class)
  (:require [taoensso.carmine :as car :refer (wcar)]
            [clojure.data.json :as json]
            [lionsandhumans
             [config :as conf]]))

(defmacro wcar* 
  [& body] 
  `(car/wcar (-> conf/params :redis :server-conn) ~@body))

(defn db-read
  [db-key]
  (let
    [db-entry (wcar* (car/get db-key))]
    (json/read-str db-entry)))

(defn db-write
  [db-key value]
  (wcar* (car/set db-key (json/write-str value))))
