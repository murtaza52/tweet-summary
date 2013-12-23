(ns tweet-summary.core
  (:require [tweet-summary.http :refer [async-request]]
            [tweet-summary.request-options :refer [get-option]]
            [clojure.core.async :refer [<!!]]
            [tweet-summary.utils.date :refer [string-to-date]]
            [tweet-summary.processors :refer [tweet-processor]]))

(def names ["weavejester", "ibdknox", "nathanmarz", "technomancy", "odersky", "craigandera", "ctford", "ghoseb", "trptcolin", "rkneufeld"])

(defrecord Tweet [text date])

(defn make-tweet-maps
  [tweets]
  (map #(Tweet. (% "text") (string-to-date (% "created_at")))
        tweets))

(->> (map get-option names)
     (map async-request)
     (map <!!)
     doall
     flatten
     make-tweet-maps
     tweet-processor)

