(ns tweet-summary.core
  (:require [tweet-summary.http :refer [async-request]]
            [tweet-summary.options :refer [get-option]]
            [tweet-summary.utils.date :refer [string-to-date date-to-string]]
            [clojure.string :as string]
            [clojure.data.priority-map :refer [priority-map-by]]
            [clojure.core.async :refer [chan go !< !<< >! >!!]]))

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

;; (defn top-words
;;   [in out]
;;   (go
;;    (->> in
;;         !<
;;         (map :text)
;;         (mapcat #(string/split % #" "))
;;         frequencies
;;         (into (priority-map-by >))
;;         (take 100)
;;         (into (priority-map-by >))
;;         (>! out))))


;; (defn top-words-chan
;;   [in]
;;   (go
;;    (->> in
;;         !<
;;         top-words
;;         (>! (chan)))))

(defn tweets-by-hour
  [coll]
  (->> coll
       (map :date)
       (map date-to-string)
       frequencies))
(def processors [top-words tweets-by-hour])

(def process-tweets #(vector (top-words %) (tweets-by-hour %)))

(->> (map get-option names)
     (map async-request)
     (map <!!)
     doall
     flatten
     make-tweet-maps
     process-tweets)

