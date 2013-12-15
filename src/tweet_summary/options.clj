(ns tweet-summary.options
  (:require [client-oauth2.oauth-header :as oauth]
            [tweet-summary.config :refer [config]]))

(def default-options
  {:url "https://api.twitter.com/1.1/statuses/user_timeline.json"
   :method :get
   :query-params {:count 200}})

(def get-auth-header
  (fn [opt]
    ((oauth/get-auth-header-graph
      :consumer-key (config :consumer-key)
      :consumer-secret (config :consumer-secret)
      :request-options opt
      :oauth-token (config :access-token)
      :oauth-token-secret (config :access-token-secret))
     :auth-header)))

(defn get-option
  [screen-name]
  (as-> (assoc-in default-options [:query-params :screen_name] screen-name)
        opt
        (->> (get-auth-header opt)
             (assoc-in opt [:headers "Authorization"]))))

