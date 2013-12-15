(ns tweet-summary.core
  (:require [tweet-summary.http :refer [async-request]]
            [tweet-summary.options :refer [get-option]]
            [tweet-summary.utils.date :refer [string-to-date date-to-string]]
            [clojure.string :as string]
            [clojure.data.priority-map :refer [priority-map-by]]
            [clojure.core.async :refer [<!!]]))

;; (def screen-names ["weavejester", "ibdknox", "nathanmarz", "technomancy", "odersky", "craigandera", "ctford", "ghoseb", "trptcolin", "rkneufeld"])

(def names ["weavejester"])

(defrecord Tweet [text date])

(defn make-tweet-maps
  [tweets]
  (map #(Tweet. (% "text") (string-to-date (% "created_at")))
        tweets))

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

(def process-tweets #(vector (top-words %) (tweets-by-hour %)))

(->> (map get-option names)
     (map async-request)
     (map <!!)
     doall
     flatten
     make-tweet-maps
     process-tweets)
