(ns analyze-runs.gpx-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [clj-time.format :as tf]
            [analyze-runs.gpx :refer :all]))

(def points-from-sample-file (get-points "test/sample.gpx"))

(deftest should-read-3-records-from-sample-file
  (is (= 3 (count points-from-sample-file))))

(deftest assert-time-first-point
  (let [first-point (first points-from-sample-file)]
    (is (= (:time first-point) (tf/parse "2016-02-17T17:35:18Z")))))

(deftest assert-elevation-first-point
  (let [first-point (first points-from-sample-file)]
    (is (= (:elevation first-point) 37.083679))))

(deftest assert-lat-first-point
  (let [first-point (first points-from-sample-file)]
    (is (= (:lat first-point) 50.671865))))

(deftest assert-lon-first-point
  (let [first-point (first points-from-sample-file)]
    (is (= (:lon first-point) 7.187874))))

(comment deftest assert-heart-rate-first-point
  (let [first-point (first points-from-sample-file)]
    (is (= (:heart-rate first-point) 114))))

(deftest assert-elapsed-time
  (is (= 3 (calculate-time points-from-sample-file))))

(deftest assert-distance
  (is (= 0.008250017987846405 (calculate-distance points-from-sample-file))))
