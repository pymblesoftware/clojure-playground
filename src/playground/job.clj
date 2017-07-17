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
    ;; FIXME: 1) Invoke the dispatcher here!
    (swap! jobs assoc id job)
    job))

(defn url-of [job]
  (str "/jobs/" (:id @job)))

(defn create-job-handler [passenger lat lng]
  (when-not (valid-passenger? passenger)
    (bad-request "invalid passenger"))
  (when-not (and (valid-lat? lat) (valid-lng? lng))
    (bad-request "invalid lat/lng"))
  (if-let [job (create-job! passenger lat lng)]
    
    (ring.util.response/created (url-of job))
    {:status 400 :body "Bad request"}))

(def client-valid-keys [:id :lat :lng])

(defn sanitize [job]
  (select-keys @job client-valid-keys))

(defn get-job-handler [job-id]
  (if-let [job (get @jobs job-id)]
    (ring.util.response/response (sanitize job))
    {:status 404}))

(defroutes job-routes
  (GET "/:job-id" [job-id] (get-job-handler job-id))
  (POST "/" [passenger lat lng] (do (println passenger lat lng) (create-job-handler passenger lat lng))))
