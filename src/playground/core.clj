(ns playground.core
  (:require [gocatch.subsystems.core :refer :all]
            [clojure.java.jdbc :as j]
            [yesql.core :refer [defqueries]]
            [taoensso.timbre :as timbre]
            [playground.routes :refer [example-routes] ])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (start-subsystem  :taximate-db)
  (start-subsystem :http #'example-routes)
  (timbre/info "Playground is running."))







