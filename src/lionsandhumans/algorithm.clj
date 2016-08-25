(ns lionsandhumans.algorithm
  (:gen-class))

(defn- human?
  [string]
  (not (nil? (re-matches #"H\d+" string))))


(defn zip
 [colla collb]
 (partition 2 (interleave colla collb)))


(defn same-boat?
  [boat1 boat2]
  (let
    [passengers1 (select-keys boat1 [:passengerA :passengerB])
     passengers2 (select-keys boat2 [:passengerA :passengerB])
     pairs (map vals [passengers1 passengers2])
     first-letters (map #(map first %) pairs)]
    (apply = first-letters)))


(defn- try-decision ;;DONT LIKE THIS, but it works
  [boat shoreA shoreB]
  (let
    [pA (:passengerA boat)
     pB (:passengerB boat)
     shore (if (= "shoreA" (:shore boat)) shoreB shoreA)
     balance (- (count (filter #(re-matches #"H\d+" %) shore)) 
                (count (filter #(re-matches #"L\d+" %) shore))) 
     new-balance (cond
                  (and (human? pA) (human? pB)) (- 2 balance)
                  (or (and (human? pA) (nil? pB)) 
                      (and (human? pB) (nil? pA))) (dec balance) 
                  :otherwise balance)]
    (>= new-balance 0)))


(defn- update-shore
  [boat shore shore-name]
  (if (= shore-name (:shore boat))
    (apply conj shore (filter #(not= "empty" %) 
                              [(:passengerA boat) (:passengerB boat)]))
    (remove #{(:passengerA boat) (:passengerB boat)} shore)))


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
     possible-decisions (map #(decide %
                                      (update-shore % shoreA "shoreA")
                                      (update-shore % shoreB "shoreB") (inc n))
                             possible-boats) 
     result (filter #(not (nil? (first %))) 
                    (zip possible-decisions possible-boats))]
    
    ;(println n " shoreA: " shoreA)
    ;(println n " shoreB: " shoreB)
    ;(println n " previous-boat: " previous-boat)
    ;(println n " source: " source)
    ;(println n " destination: " destination)
    ;(println n " destination-name: " destination-name)
    ;(println n " all-boats: " all-boats)
    ;(println n " possible-boats: " possible-boats)
    ;(println n " possible-decisions: " possible-decisions)
    ;(println n " result: " result)
    (apply conj (first result))
    ;result
    )))


(defn compute-decisions
  [x]
  (decide {:passengerA "none" :passengerB "none" :shore "shoreA"} x [] 0))
;;THE END
;(compute-decisions ["H1" "L1" "H2"])
