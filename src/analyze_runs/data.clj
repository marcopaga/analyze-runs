(ns analyze-runs.data
  (:require [analyze-runs.input :refer [runs-channel fill-input-channel]]
            [clojure.core.async :as async :refer [>! <! >!! <!! go chan buffer close! thread
                                                  alts! alts!! timeout]])
  (:gen-class))

(defonce data (atom {}))

(defn read-from-stream []
  (async/go-loop []
    (let [run (<! runs-channel)]
      (swap! data assoc (:date run) run)) (recur))
  )

