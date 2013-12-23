(ns tweet-summary.processors
  (:require [tweet-summary.utils.date :refer [date-to-string]]
            [clojure.string :as string]
            [clojure.data.priority-map :refer [priority-map-by]]))

(defn top-words
  [coll]
  (->> coll
       (map :text)
       (mapcat #(string/split % #" "))
       frequencies
       (into (priority-map-by >))
       (take 100)
       (into (priority-map-by >))))

(defn tweets-by-hour
  [coll]
  (->> coll
       (map :date)
       (map date-to-string)
       frequencies))

(defn parallel-processor
  [fns]
  (fn [coll]
    (->> fns
         (map (fn[f] (future (f coll))))
         (map deref))))

(def tweet-fns [top-words tweets-by-hour])

(def tweet-processor (parallel-processor tweet-fns))
