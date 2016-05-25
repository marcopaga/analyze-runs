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
(def output-channel (chan 20))

(defn distance [file]
  (-> file .getAbsolutePath get-points-from-file calculate-distance))

(async/pipeline (.. Runtime getRuntime availableProcessors) output-channel (map distance) gpx-files-channel)

(defn find-files [path]
  (let [all-direntries (file-seq (io/file path))
        all-files (filter #(.isFile %) all-direntries)
        all-gpx-files (filter #(s/includes? % ".gpx") all-files)]
    all-gpx-files))

(defn find-files-dropbox []
  (find-files "/Users/marco/Dropbox/Apps/iSmoothRun/Export"))

(defn find-files-annex []
  (find-files "/Users/marco/annex/archive/Laeufe"))

(defn find-all-files []
  (concat (find-files-dropbox) (find-files-annex)))

(defn fill-input-channel []
  (async/pipe (async/to-chan (find-all-files)) gpx-files-channel false))

(defn test-pipeline []
  (let [number-of-files (count (find-all-files))]
    (fill-input-channel)
    (doseq [n (range number-of-files)]
      (prn (<!! output-channel)))
    (prn "done")))

(defn -main
  [& args]
  (str (find-files)))

                                        ; map => 53 s
                                        ; pmap => 20s
                                        ; core.async 23s
