(ns tweet-summary.twitter-api
  (:require [twitter.api.restful :refer [statuses-user-timeline]]))

(defn fetch-tweets
  [creds user tweet-count]
  (statuses-user-timeline :oauth-creds creds :screen-name user :count tweet-count :include_rts false))
