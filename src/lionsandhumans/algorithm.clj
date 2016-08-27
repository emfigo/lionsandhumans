(ns lionsandhumans.algorithm
  (:gen-class))



(defn- lion?
  [string]
  (not (nil? (re-matches #"L\d+" string))))


(defn- human?
  [string]
  (not (nil? (re-matches #"H\d+" string))))

(defn zip
 [& args]
 (apply map vector args))


(defn same-boat?
  [boat1 boat2]
  (let
    [passengers1 (map (comp str first) (vals (select-keys boat1 [:passengerA :passengerB])))
     passengers2 (map (comp str first) (vals (select-keys boat2 [:passengerA :passengerB])))
     passengers2' (reverse passengers2)]
    (or (= passengers1 passengers2) (= passengers1 passengers2'))))


(defn- try-decision ;;DONT LIKE THIS, but it works
  [boat shoreA shoreB]
  (let
    [pA (:passengerA boat)
     pB (:passengerB boat)
     shore (if (= "shoreA" (:shore boat)) shoreB shoreA)
     humans (filter human? shore)
     lions (filter lion? shore)
     nH (count (remove (set (vals (select-keys boat [:passengerA :passengerB]))) 
                       humans)) 
     nL (count (remove (set (vals (select-keys boat [:passengerA :passengerB]))) 
                       lions))
     balance (- nH nL)]
;    (println pA)
;    (println pB)
;    (println shore)
;    (println humans)
;    (println lions)
;    (println nH)
;    (println nL)
    (or (>= balance 0) (= nH 0))))


;(try-decision {:passengerA "H1"
;               :passengerB "L1"
;               :shore "shoreB"}
;              ["H1" "L1" "L2"]
;              [])


(defn- update-shore
  [boat shore shore-name]
  (if (= shore-name (:shore boat))
    (apply conj shore (filter #(not= "empty" %) 
                              (vals (select-keys 
                                     boat 
                                     [:passengerA :passengerB]))))
    (remove (set (vals (select-keys boat
                                    [:passengerA :passengerB]))) shore)))



(defn- generate-boats
  [sh shname]
  (filter #(not (nil? %)) 
          (let
            [h1 (first (filter #(re-matches #"H\d+" %) sh))
             h2 (second (filter #(re-matches #"H\d+" %) sh))
             l (first (filter #(re-matches #"L\d+" %) sh))]
            [(if (not (or (nil? h1) (nil? h2)))
               {:passengerA h1 :passengerB h2 :shore shname})
             (if (not (or (nil? h1) (nil? l))) 
               {:passengerA h1 :passengerB l :shore shname})
             (if (not (nil? h1))
               {:passengerA h1 :passengerB "empty" :shore shname})])))


(defn decide 
  [previous-boat shoreA shoreB n]
  (if
    (empty? shoreA)
    []
    (let
      [source (if (= "shoreA" (:shore previous-boat)) shoreA shoreB)
       destination (if (= "shoreA" (:shore previous-boat)) shoreB shoreA)
       destination-name (if (= "shoreA" (:shore previous-boat)) "shoreB" "shoreA")
       all-boats (generate-boats source destination-name)

       possible-boats (filter #(and (try-decision % shoreA shoreB) 
                                    (not (same-boat? previous-boat %))) 
                              all-boats)
       ]
      (if (empty? possible-boats)
        nil
        (let
          [possible-decisions (map #(decide %
                                            (update-shore % shoreA "shoreA")
                                            (update-shore % shoreB "shoreB") (inc n))
                                   possible-boats) 
           results (zip possible-decisions possible-boats) 
           result (filter #(not (nil? (first %))) 
                          (zip possible-decisions possible-boats))]
          ;(println n " shoreA: " shoreA)
          ;(println n " shoreB: " shoreB)
          (println n " PREVIOUS-Boat: " previous-boat)
          (println n " source: " source)
          (println n " destination: " destination)
          ;(println n " destination-name: " destination-name)
          ;(println n " all-boats: " all-boats)
          (println n " possible-Boats: " possible-boats)
          ;(println n " possible-Decisions: " possible-decisions)
          ;(println n " ############RESULT##############: " results)
          (apply conj (first result)))))))


(defn compute-decisions
  [x]
  (decide {:passengerA "none" :passengerB "none" :shore "shoreA"} x [] 0))
;;THE END
(compute-decisions ["H1" "H2" "L1" "L2"])
