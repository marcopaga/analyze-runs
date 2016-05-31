(ns analyze-runs.core
  (:require
            [analyze-runs.app :refer :all]
            [com.stuartsierra.component :as component])
  (:import [org.apache.commons.daemon Daemon DaemonContext])
  (:gen-class
   :implements [org.apache.commons.daemon.Daemon]))

(def state (atom {}))

(defn init [args]
  (swap! state assoc :running true))

(defn start []
  (component/start (runs-system {}))
  (while (:running @state)
    (println "tick")
    (Thread/sleep 2000)))

(defn stop []
  (swap! state assoc :running false))

;; Daemon implementation

(defn -init [this ^DaemonContext context]
  (init (.getArguments context)))

(defn -start [this]
  (future (start)))

(defn -stop [this]
  (stop))

(defn -destroy [this])

;; Enable command-line invocation
(defn -main [& args]
  (init args)
  (start))
