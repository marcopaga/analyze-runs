(ns analyze-runs.core
  (:require [analyze-runs.gpx :refer :all]
            [analyze-runs.input :refer [test-pipeline]]
            [clojure.java.io :as io]
            )
  (:gen-class))

(defn -main
  [& args]
  (test-pipeline))
