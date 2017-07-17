(defproject playground "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"] 
                 [com.gocatch/subsystems "2.8.7"]
                 [clojure-future-spec "1.9.0-alpha11"]
                 [org.clojure/test.check "0.9.0"]]
  :repositories [["releases" 
                  {:url "https://nexus.gocatchapp.com/artifactory/engineering-releases/"
                   :sign-releases false}]
                  ["snapshots" 
                   {:url "https://nexus.gocatchapp.com/artifactory/engineering-snapshots/" :sign-releases false}]]

  :main ^:skip-aot playground.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
)
