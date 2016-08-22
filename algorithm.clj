(ns lionsandhumans.algorithm)


(defn try-decisions
  [coll [hn ln sh]]
  (let
    [hs (filter #(re-matches #"H\d+" %) coll) 
     h1 (first hs)
     h2 (second hs)
     l (first (filter #(re-matches #"L\d+" %) coll))
     human_and_lion (decide 
                     (remove #{h1 l} coll) 
                     [(dec hn) (dec ln) (- sh)])
     human_alone    (decide 
                     (remove #{h1} coll) 
                     [(dec hn) ln (- sh)])
     human_and_human (decide 
                      (remove #{h1 h2} coll) 
                      [(- hn 2) ln (- sh)])]
    (cond
     human_and_lion (conj human_and_lion [h1 l])
     human_and_human (conj human_and_human [h1 h2])
     human_alone (conj human_alone [h1 nil])
     :else nil)))

(defn decide
  [coll [hn ln sh :as state]]
  (if (= state [0 0 -1])
    []
    (if (< hn ln)
      nil
      (try-decisions coll state))))

(def coll ["H1" "L1" "H2" "L2"])
(try-decisions coll [2 2 1])
(def h1 (first (filter #(re-matches #"H\d+" %) coll)))
(def l (first (filter #(re-matches #"L\d+" %) coll)))
(remove #{h1 l} coll)

(defn compute-decisions
  [x]
  (let 
    [ammount (/ (count x) 2)]
    (decide x [ammount ammount 1] )))

