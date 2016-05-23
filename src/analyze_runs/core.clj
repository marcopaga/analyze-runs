(ns analyze-runs.core
  (:require [analyze-runs.gpx :refer :all]
            [clojure.java.io :as io]
            [clojure.string :as s]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.core.async :as async :refer [>! <! >!! <!! go chan buffer close! thread
                                                  alts! alts!! timeout]])
  (:gen-class))

(def gpx-files-channel (chan))

(defn find-files []
  (find-files "/Users/marco/Dropbox/Apps/iSmoothRun/Export"))

(defn find-files-annex []
  (find-files "/Users/marco/annex/archive/Laeufe"))

(defn find-files [path]
  (let [all-direntries (file-seq (io/file path))
        all-files (filter #(.isFile %) all-direntries)
        all-gpx-files (filter #(s/includes? % ".gpx") all-files)]
    all-gpx-files))

(defn distance [file]
  (-> file .getAbsolutePath get-points-from-file calculate-distance))

(defn -main
  [& args]
  (str (find-files)))
