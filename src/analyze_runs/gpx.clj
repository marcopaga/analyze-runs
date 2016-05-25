(ns analyze-runs.gpx
  "This code is from: https://github.com/honza/clj-gpx and was distributed under the BSD License"
  (:require [clojure.xml :refer :all]
            [clojure.algo.generic.math-functions :refer :all]
            [clj-time.format :as tf]
            [clj-time.core :as tc]
            [clojure.pprint :refer [pprint]]))

(def R 6367)
 
(defn- rad [x] 
  (* x (/ Math/PI 180)))
 
(defn- meter [x] (* x 1000))

;; https://gist.github.com/frankvilhelmsen/1787462
(defn- haversine [position destination]
  (let [square_half_chord 
          (+ (pow (sin (/ (rad (- (destination :lat) (position :lat))) 2)) 2) 
             (* (cos (rad (position :lat))) 
                (cos (rad (destination :lat))) 
                (pow (sin (/ (rad (- (destination :lon) (position :lon))) 2)) 2)))
        angular_distance (* (asin (sqrt square_half_chord)) 2)]
    (* angular_distance R)))
 
(defn- parse-gpx-file [path]
  (parse (java.io.ByteArrayInputStream. (.getBytes (slurp path)))))

(defn- parse-gpx-string [string]
  (parse (java.io.ByteArrayInputStream. (.getBytes string))))

(defn- tag? [tag m]
  (when (= tag (:tag m)) m))

(defn find-tag [coll tag]
  (some (partial tag? tag) coll))

(defn- transform-trkpt [p]
  (let [t   (find-tag (:content p) :time)
        ts  (tf/parse (first (:content t)))
        e   (find-tag (:content p) :ele)
        ele  (read-string (first (:content e)))
        lat (read-string (get-in p [:attrs :lat]))
        lon (read-string (get-in p [:attrs :lon]))]
    {:time ts
     :elevation ele
     :lat lat
     :lon lon
     }))

(defn- get-data [xml]
  (:content (find-tag (:content xml) :trk)))

(defn- get-points[source parser-fn]
  (let [raw (get-data (parser-fn source))
        trks (:content (find-tag raw :trkseg))]
    (sort-by :time (map transform-trkpt trks))))

(defn get-points-from-string [string]
  (get-points string parse-gpx-string))

(defn get-points-from-file [path]
  (get-points path parse-gpx-file))

(defn calculate-time [coll]
  "in seconds"
  (tc/in-minutes
    (tc/interval (:time (first coll))
                 (:time (last coll)))))

(defn calculate-distance [coll]
  (transduce (map #(haversine (first %) (second %))) + (partition 2 1 coll)))

(defn rround [n]
  (/ (round (* n 100)) 100.0))

(comment defn -main [& args]
  (when-let [path (first args)]
    (let [points        (get-points path)
          elapsed-time  (calculate-time points)
          distance      (rround (calculate-distance points))
          average-speed (rround (/ distance (/ elapsed-time 3600)))]
      (println "Distance:     " distance "km")
      (println "Elapsed time: " elapsed-time "min")
      (println "Average speed:" average-speed "km/h"))))
