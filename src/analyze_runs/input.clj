(ns analyze-runs.input
  (:require [analyze-runs.gpx :refer :all]
            [clojure.java.io :as io]
            [clojure.string :as s]
            [clj-time.core :as t]
            [clj-time.format :as tf]
            [clojure.core.async :as async :refer [>! <! <! go chan pipeline pipe to-chan close!]]
            [com.stuartsierra.component :as component])
  (:gen-class))

(def week-formatter (tf/formatters :weekyear-week))
(def date-formatter (tf/formatter "yyyy-MM-dd"))
(def time-formatter (tf/formatter "HH:mm:ss"))

(defn handle-file [file]
  (let [points (-> file .getAbsolutePath get-points-from-file)
        distance (.format (java.text.DecimalFormat. "#.##") (calculate-distance points))
        duration (calculate-time points)
        start (:time (first points))
        year-week (tf/unparse week-formatter start)
        date (tf/unparse date-formatter start)
        time (tf/unparse time-formatter start)
        ]
    {:year-week year-week
     :date date :time time :distance distance :duration duration :joda-date start}))

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

(defn fill-input-channel [gpx-files-channel]
  (pipe (to-chan (find-all-files)) gpx-files-channel false))

(defrecord Input [runs-channel]
  component/Lifecycle
  (start [this]
    (let [gpx-files-channel (chan)]
      (pipeline (.. Runtime getRuntime availableProcessors) runs-channel (map handle-file) gpx-files-channel)
      (fill-input-channel gpx-files-channel)
      (assoc this :gpx-files-channel gpx-files-channel)))
  (stop [this]
    (close! (:gpx-files-channel this))
    (assoc this :gpx-files-channel nil)))

(defn new-input-component [runs-channel]
  (map->Input {:runs-channel runs-channel}))
