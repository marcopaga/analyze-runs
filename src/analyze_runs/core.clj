(ns analyze-runs.core
  (:require [analyze-runs.gpx :refer :all]
            [clojure.java.io :as io]
            [clojure.string :as s])
  (:gen-class))

(defn find-files []
  (let [all-direntries (file-seq (io/file "/Users/marco/Dropbox/Apps/iSmoothRun/Export"))
        all-files (filter #(.isFile %) all-direntries)
        all-gpx-files (filter #(s/includes? % ".gpx") all-files)]
    all-gpx-files))

(defn distance [file]
  (-> file .getAbsolutePath get-points calculate-distance))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println (map distance (find-files))))
