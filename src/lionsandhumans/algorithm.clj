(ns lionsandhumans.algorithm
  (:gen-class))




(defn try-decision
  [boat shoreA shoreB])


(defn update-shore
  [boat shore shore-name]
  (if (= shore-name (:shore boat))
    (conj shore (:passengerA boat) (:passengerB boat))
    (remove #{(:passengerA boat) (:passengerB boat)} shore)))


(defn generate-boats
  [sh shname]
  (let
    [h1 (first (filter #(re-matches #"H\d+" %) sh))
     h2 (second (filter #(re-matches #"H\d+" %) sh))
     l (first (filter #(re-matches #"L\d+" %) sh))]
   [(if (not (or (nil? h1) (nil? h2)))
     {:passengerA h1 :passengerB h2 :shore shname})
   (if (not (or (nil? h1) (nil? l))) 
     {:passengerA h1 :passengerB l :shore shname})
   (if (not (nil? h1))
     {:passengerA h1 :passengerB nil :shore shname})]))


(defn decide 
  ;;WIP
  ;;lack of handling empty boat
  ;;
  [previous-boat shoreA shoreB]
  (if
    (empty? shoreA)
    []
    (let
    [source (if (= "shoreA" (:shore previous-boat)) shoreA shoreB)
     destination (if (= "shoreA" (:shore previous-boat)) shoreB shoreA)
     all-boats (generate-boats source (:shore previous-boat))
     possible-boats (filter #(try-decision % shoreA shoreB) all-boats)
     possible-decisions (map #(decide %
                                      (update-shore % shoreA "shoreA")
                                      (update-shore % shoreB "shoreB"))
                             possible-boats)]
    (first possible-decisions)))


(defn compute-decisions
  [x]
  (decide {:shore "shoreA"} x []))
;;THE END
