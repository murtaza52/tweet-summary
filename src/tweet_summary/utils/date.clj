(ns tweet-summary.utils.date
  (:require [clj-time.format :refer [unparse parse formatter formatters]]
            [tweet-summary.config :refer [config]]))

(defn parse-date
  "Returns a date parser based on the format. Format can be either a custom string or clj-time keyword."
  [format]
  (fn [date-string]
    (cond
     (keyword? format) (parse (formatters format) date-string)
     (string? format) (parse (formatter format) date-string))))

(def custom-formatter (formatter "dd MMMM',' h':00' a"))

(def date-to-string #(unparse custom-formatter %))

(def string-to-date (parse-date "E MMM dd HH:mm:ss Z YYYY"))
