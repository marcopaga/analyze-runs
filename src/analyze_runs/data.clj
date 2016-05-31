(ns analyze-runs.data
  (:require
            [clojure.core.async :as async :refer [>! <! >!! <!! go chan buffer close! thread alts! alts!! timeout]]
            [com.stuartsierra.component :as component])
  (:gen-class))

(defn read-from-stream [data-component]
  (async/go-loop []
    (let [run (<! (:runs-channel data-component))]
      (prn (str "Read " run " from channel"))
      (swap! (:data-atom data-component) assoc (:date run) run)) (recur))
  )

(defn data-atom [data-component]
  (:data-atom data-component))

(defrecord Data [runs-channel]
  component/Lifecycle
  (start [component]
    (let [new-component (assoc component :data-atom (atom {}) :runs-channel runs-channel)]
      (read-from-stream new-component)
      new-component)
    )
  (stop [component]
    (assoc component :data-atom nil :runs-channel nil)))

(defn new-data-component
  [runs-channel]
  (map->Data {:runs-channel runs-channel}))
