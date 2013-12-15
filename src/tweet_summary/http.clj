(ns tweet-summary.http
  (:require [org.httpkit.client :as http]
            [cheshire.core :as cheshire]
            [clojure.core.async :refer [put! <! chan go]]))

(defn- http-channell
  [opt]
  (let [c (chan)]
    (http/request opt
                  #(put! c %))
    c))

(defn async-request
  [opt]
  (go
   (-> opt
       http-channell
       <!
       :body
       cheshire/parse-string)))
