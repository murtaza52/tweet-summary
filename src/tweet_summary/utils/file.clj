(ns tweet-summary.utils.file
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]))

(defn read-edn
  [file-path]
  (-> file-path
      io/resource
      slurp
      edn/read-string))
