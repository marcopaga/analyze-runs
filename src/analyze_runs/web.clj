(ns analyze-runs.web
  (:require [analyze-runs.gpx :refer :all]
            [analyze-runs.data :refer [data]]
            [analyze-runs.input :refer [fill-input-channel]]
            [analyze-runs.data :refer [read-from-stream]]
            [compojure.core :refer :all]
            [compojure.route :as route])
  (:gen-class))

(defroutes app
  (GET "/" [] "Hello from compojure!")
  (POST "/" {body :body} (-> (slurp body) get-points-from-string calculate-distance str)))
