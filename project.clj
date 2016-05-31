(defproject analyze-runs "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [com.stuartsierra/component "0.3.1"]
                 [clj-time "0.8.0"]
                 [org.clojure/algo.generic "0.1.2"]
                 [compojure "1.5.0"]
                 [org.clojure/core.async "0.2.374"]
                 [commons-daemon/commons-daemon "1.0.15"]]
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler analyze-runs.web/app}
  :main ^:skip-aot analyze-runs.core
  :target-path "target/%s"
  :profiles {
  {:uberjar {:aot :all}}
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]]}}})
