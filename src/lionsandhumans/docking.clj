(ns lionsandhumans.docking)

(defn dock-in
  [shore]
  (case shore
    "A" (println "now it should add records to shoreA db")
    "B" (println "now it should add records to shoreB db")
    (println "Pirate ship docked!")))
