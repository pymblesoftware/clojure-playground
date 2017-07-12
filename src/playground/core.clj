(ns playground.core
  (:require [gocatch.subsystems.core :refer :all]
            [clojure.java.jdbc :as j]
            [yesql.core :refer [defqueries]]
            [playground.routes :refer [example-routes] ])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!")

  (start-subsystem  :taximate-db)
  (start-subsystem :http #'example-routes)
  )

(defn my-fun
  [] "Hello")


;(def foo (j/query taximate-db ["select * from Job limit 5;"]))

;(def all_tables (j/query taximate-db ["show tables;"]))

;(def x1 (first foo))

;(defn mungei
;  [m]
;  (assoc m :vehicle_type (keyword (clojure.string/lower-case (m :vehicle_type)))))
;
;(defn munge2 [m]
;  (update m
;          :vehicle_type
;          #(keyword (clojure.string/lower-case %))))
;
;(defn munge3 [m]
;  (update m
;          :vehicle_type
;          (fn [v] (keyword (clojure.string/lower-case v)))))
;
;
;(def x1  {:rate_card_id nil,
;              ;; :creation_time
;              ;; #object[org.joda.time.DateTime 0x20dbd12b "2015-06-09T18:19:20.000Z"],
;              :repost_of 1,
;              :instructions "Pick me up in the alley",
;              :tip 200,
;              :pickup 753,
;              :id 2,
;              :vehicle_type :taxi,
;              :payment_method_id nil,
;              :origin 754,
;              :destination 752,
;          :passenger_id "1234"})
;
;(mungei x1)
;(munge2 x1)
;
;(println (range 100))
;
;
;(def ^:dynamic *foo*)
;
;(def blah 7)
;
;
;(^{:a 1 :b 2} [1 2 3])
;
;(def ^:dynamic *dyn*)
;
;(binding [*dyn* "Hello world"])
;(println *dyn*)
;
;(defn bar [f x]
;  (dotimes [i 50]
;    (Thread/sleep 1000)
;    (println (f x))))
