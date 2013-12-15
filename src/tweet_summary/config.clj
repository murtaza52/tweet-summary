(ns tweet-summary.config
  (:require [tweet-summary.utils.file :as file]))

(def conf-file "config.edn")

(def config (file/read-edn conf-file))
