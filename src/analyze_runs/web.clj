(ns analyze-runs.web
  (:require [analyze-runs.gpx :refer :all]
            [compojure.core :refer :all]
            [compojure.route :as route])
  (:gen-class))

(defn distance [body]
  (-> (slurp body) get-points-from-string calculate-distance str))

(defn stupid-json [distance]
  (str "{\"distance\": " distance "}"))
(defroutes app
  (GET "/" [] "Hello from compojure!")
  (POST "/" {body :body} (stupid-json (distance body))))
