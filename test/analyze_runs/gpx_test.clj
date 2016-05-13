(ns analyze-runs.gpx-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [clj-time.format :as tf]
            [analyze-runs.gpx :refer :all]))

(def points-string "<gpx><trk><name>Test</name><trkseg><trkpt lat='1' lon='2'><ele>4</ele><time>2016-05-13T11:11:11Z</time></trkpt><trkpt lat='2' lon='3'><ele>4</ele><time>2016-05-13T11:15:11Z</time></trkpt></trkseg></trk></gpx>")

(def points-from-sample-file (get-points-from-file "test/sample.gpx"))

(def first-point (first points-from-sample-file))

(deftest should-read-3-records-from-sample-file
  (is (= 3 (count points-from-sample-file))))

(deftest assert-time-first-point
  (is (= (:time first-point) (tf/parse "2016-02-17T17:35:18Z"))))

(deftest assert-elevation-first-point
  (is (= (:elevation first-point) 37.083679)))

(deftest assert-lat-first-point
  (is (= (:lat first-point) 50.671865)))

(deftest assert-lon-first-point
  (is (= (:lon first-point) 7.187874)))

(deftest assert-elapsed-time
  (is (= 3 (calculate-time points-from-sample-file))))

(deftest assert-distance
  (is (= 0.008250017987846405 (calculate-distance points-from-sample-file))))

(deftest assert-points-from-string
  (is (= 2 (count (get-points-from-string points-string) ))))
