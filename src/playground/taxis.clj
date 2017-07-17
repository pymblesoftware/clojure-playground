(ns playground.taxis
  (:require [gocatch.subsystems.utils :refer [distance-between ignore-errors]]
            [gocatch.subsystems.http.middleware :refer [bad-request not-found]]
            [compojure.core :refer :all]
            [ring.util.response :refer [response]]
            [playground.spec :refer [valid-lat? valid-lng?]]
            [playground.job :refer [jobs]]))

(def max-distance
  "The max distance, in meters, which a client is allowed to see a taxi"
  500)

(defn safe-parse-float [s]
  (ignore-errors (Double/parseDouble s)))

(defn make-taxi
  ([name] (atom {:name name
                 :lat 0
                 :lng 0}))
  ([name lat lng] (atom {:name name
                         :lat lat
                         :lng lng})))

(defn move-taxi [taxi delta-lat delta-lng]
  (-> taxi
      (update :lng + delta-lng)
      (update :lat + delta-lat)))

(defn update-taxi-location [taxi lat lng]
  (-> taxi
      (assoc :lng lng)
      (assoc :lat lat)))

(defn move-in-background [taxi duration]
 (future
   (dotimes [i duration]
     (Thread/sleep 2000)
     (swap! taxi move-taxi (rand) (rand)))))

(def taxis (atom {"A" (make-taxi "A")
                  "B" (make-taxi "B")}))

(defn get-taxi [name]
  (@taxis name))

(defn get-taxi-detail [taxi-name]
  (deref (get-taxi taxi-name)))

(defn- get-nearest-taxis-wrapper [taxis]
  (fn [lat lng distance]
    (let [distance-to (partial distance-between [lat lng])
          taxis (vals (deref taxis))
          selected (filter (fn [taxi]
                             (> distance (distance-to [(@taxi :lat ) (@taxi :lng)])))
                           taxis)]
      selected)))

(defn get-available-jobs [taxi jobs]
  (let [all-jobs (vals @jobs)
        {t-lat :lat t-lng :lng} @taxi
        x (reduce (fn [available-jobs job]
                    (let [distance (distance-between [t-lat t-lng] [(@job :lat) (@job :lng)])]
                      (if (>= max-distance distance)
                        (into available-jobs [@job])
                        available-jobs)))
                  []
                  all-jobs)]
    x))




(defn- get-location [obj]
  [(:lat @obj) (:lng @obj)])

(defn- jobs-within-range? [loc job]
  (>= max-distance (distance-between loc (get-location job))))

(defn get-available-jobs [taxi jobs]
  ;; FIXME: 2) ask dispatcher for any available jobs for this taxi
  (filter (partial jobs-within-range? (get-location taxi))
          (vals jobs)))

;; FIXME: 3)
;; write dispatcher codeb.


(def
  ^{:arglists '([lat lng distance])}
  get-taxis
 "Public access method to get list of taxis near location to specified distance"
  (get-nearest-taxis-wrapper taxis))

(defn update-taxi! [taxi lat lng]
  (swap! taxi update-taxi-location lat lng))

(defn get-taxis-handler [lat lng]
  (response "OK.")
  (let [lat (safe-parse-float lat)
        lng (safe-parse-float lng)]
    (when-not (and (valid-lat? lat ) (valid-lng? lng))
      (bad-request "Invalid lat/lng supplied.\n"))
    (if (and lat lng)
      (response {:taxis (map deref (get-taxis lat lng max-distance))}))))

(defn update-taxi-handler [taxi-name {:keys [lat lng]}]
  (when-not (and (valid-lat? lat) (valid-lng? lng))
    (bad-request "Invalid lat/lng supplied.\n"))
  (if-let [taxi (get-taxi taxi-name)]
    (do
      (update-taxi! taxi lat lng)
      (response {:job-available (map deref (get-available-jobs taxi @jobs))}))
    (not-found "Taxi not found.\n")))

(defn get-taxi-detail-handler [taxi-name]
  (if-let [taxi (get-taxi-detail taxi-name)]
    (response {:taxi taxi})
    (not-found "Taxi not found.\n")))

(defroutes taxi-routes
  (GET "/" [lat lng] (get-taxis-handler lat lng))
  (PUT "/:t-name" [t-name location] (update-taxi-handler t-name location))
  (GET "/:t-name" [t-name ] (get-taxi-detail-handler t-name)))
