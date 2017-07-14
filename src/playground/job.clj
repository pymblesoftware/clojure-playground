(ns playground.job
  (:require [gocatch.subsystems.utils :refer [ignore-errors] ]
            [compojure.core :refer :all]
            [clojure.spec :as s]
            [playground.spec :refer [valid-passenger? valid-lat? valid-lng?]]
            [gocatch.subsystems.http.middleware :refer [bad-request]]))


(defn uuid [& _] (str (java.util.UUID/randomUUID)))
(def jobs (atom {}))

(defn make-job [id passenger lat lng]
  (atom {:id id
         :name passenger
         :lat lat
         :lng lng}))

(defn create-job! [passenger lat lng]
  (let [id (uuid)
        job (make-job id passenger lat lng)]
    (swap! jobs conj {id job})
    id))

(defn create-job-handler [passenger lat lng]
  (when-not (valid-passenger? passenger)
    (bad-request "invalid passenger"))
  (when-not (and (valid-lat? lat) (valid-lng? lng))
    (bad-request "invalid lat/lng"))
  (create-job! passenger lat lng))

(defroutes job-routes
  (POST "/" [passenger lat lng] (do (print passenger lat lng) (create-job-handler passenger lat lng))))


(def a [])
(conj a {:a "a"})
(do @jobs)
(create-job! "Jim" 2 0)
