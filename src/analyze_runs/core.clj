(ns analyze-runs.core
  (:require [analyze-runs.gpx :refer :all]
            [clojure.java.io :as io]
            [clojure.string :as s]
            [compojure.core :refer :all]
            [compojure.route :as route])
  (:gen-class))

(defn find-files []
  (let [all-direntries (file-seq (io/file "/Users/marco/Dropbox/Apps/iSmoothRun/Export"))
        all-files (filter #(.isFile %) all-direntries)
        all-gpx-files (filter #(s/includes? % ".gpx") all-files)]
    all-gpx-files))

(defn distance [file]
  (-> file .getAbsolutePath get-points-from-file calculate-distance))

(defroutes app
  (GET "/" [] "Hello from compojure!")
  (POST "/" {body :body} (get-points-from-string (slurp body))))

(defn -main
  [& args]
  (println (map distance (find-files))))
