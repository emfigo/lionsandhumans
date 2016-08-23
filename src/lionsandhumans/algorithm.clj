(ns lionsandhumans.algorithm
  (:gen-class))

;;(compute-decisions ["H1" "L1" "H2" "L2"])

(defn try-decisions
  [shoreA shoreB sh]
  (println "tryDEBUG# A " shoreA " B " shoreB " sh " sh)
  (if (> sh 0)
    (let ;;shoreA
      [hs (filter #(re-matches #"H\d+" %) shoreA) 
       h1 (first hs)
       h2 (second hs)
       l (first (filter #(re-matches #"L\d+" %) shoreA))
       shore-name "shoreB" 
       human_and_lion (if (or (nil? h1) (nil? l))
                        nil
                        (decide 
                         (remove #{h1 l} shoreA) 
                         (conj shoreB h1 l)
                         (- sh)))
       human_alone    (if (nil? h1)
                        nil
                        (decide 
                         (remove #{h1} shoreA) 
                         (conj shoreB h1)
                         (- sh)))
       human_and_human (if (or (nil? h2) (nil? h1)) nil
                         (decide 
                          (remove #{h1 h2} shoreA) 
                          (conj shoreB h1)
                          (- sh)))]
      (cond
       human_and_lion (conj human_and_lion 
                            {:passengerA h1 
                             :passengerB l 
                             :shore shore-name})
       human_and_human (conj human_and_human 
                             {:passengerA h1 
                              :passengerB h2 
                              :shore shore-name})
       human_alone (conj human_alone 
                         {:passengerA h1 
                          :passengerB nil 
                          :shore shore-name})
       :else nil))
    (let ;;shoreB
      [hs (filter #(re-matches #"H\d+" %) shoreB) 
       h1 (first hs)
       h2 (second hs)
       l (first (filter #(re-matches #"L\d+" %) shoreB))
       shore-name "shoreA"
       human_alone    (if (nil? h1)
                        nil
                        (decide 
                         (conj shoreA h1)
                         (remove #{h1} shoreB) 
                         (- sh)))
       human_and_lion (if (or (nil? h1) (nil? l))
                        nil
                        (decide 
                         (conj shoreA h1 l)
                         (remove #{h1 l} shoreB)
                         (- sh)))
       human_and_human (if (or (nil? h2) (nil? h1))
                         nil
                         (decide 
                          (conj shoreA h1 h2)
                          (remove #{h1 h2} shoreB) 
                          (- sh)))]
      (cond
       human_and_lion (conj human_and_lion 
                            {:passengerA h1 
                             :passengerB l 
                             :shore shore-name})
       human_and_human (conj human_and_human 
                             {:passengerA h1 
                              :passengerB h2 
                              :shore shore-name})
       human_alone (conj human_alone 
                         {:passengerA h1 
                          :passengerB nil 
                          :shore shore-name})
       :else nil))))

(defn decide
  [shoreA shoreB sh]
  (println "DEBUG# A " shoreA " B " shoreB " sh " sh)
  (if (empty? shoreA)
    []
    (let
      [HnA (count (filter #(re-matches #"H\d+" %) shoreA))
       HnB (count (filter #(re-matches #"H\d+" %) shoreB))
       LnA (count (filter #(re-matches #"L\d+" %) shoreA))
       LnB (count (filter #(re-matches #"L\d+" %) shoreB))]
      (if (or (< HnA LnA) (< HnB LnB))
        nil
        (try-decisions shoreA shoreB sh)))))

(defn compute-decisions
  [x]
  (decide x [] 1))

