(ns analyze-runs.app
  (:require [com.stuartsierra.component :as component]
            [clojure.core.async :as async :refer [chan]]
            [analyze-runs.data :refer :all]
            [analyze-runs.input :refer :all]
            )
  (:gen-class))

(defn runs-system [config-options]
  (component/system-map
   :runs-channel (chan 20)
   :input (component/using
           (new-input-component config-options) [:runs-channel])
   :data (component/using
          (new-data-component config-options) [:runs-channel])
   ))
