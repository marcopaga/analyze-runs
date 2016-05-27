(ns analyze-runs.input
  (:require [analyze-runs.gpx :refer :all]
            [clojure.java.io :as io]
            [clojure.string :as s]
            [clj-time.core :as t]
            [clj-time.format :as tf]
            [clojure.core.async :as async :refer [>! <! >!! <!! go chan buffer close! thread
                                                  alts! alts!! timeout]])
  (:gen-class))

(def week-formatter (tf/formatters :weekyear-week))
(def date-formatter (tf/formatter "yyyy-MM-dd"))
(def time-formatter (tf/formatter "HH:mm:ss"))

(def gpx-files-channel (chan))
(def runs-channel (chan 20))

(defn handle-file [file]
  (let [points (-> file .getAbsolutePath get-points-from-file)
        distance (.format (java.text.DecimalFormat. "#.##") (calculate-distance points))
        duration (calculate-time points)
        start (:time (first points))
        year-week (tf/unparse week-formatter start)
        date (tf/unparse date-formatter start)
        time (tf/unparse time-formatter start)
        ]
    {:year-week year-week :date date :time time :distance distance :duration duration}))

(async/pipeline (.. Runtime getRuntime availableProcessors) runs-channel (map handle-file) gpx-files-channel)

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
      (prn (<!! runs-channel)))
    (prn "done")))
