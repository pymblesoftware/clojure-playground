(ns playground.taxis
  (:require [gocatch.subsystems.utils :refer [distance-between ignore-errors]]))


(defn make-taxi [name]
  (atom {:name name
         :lat  0
         :lng 0}))
(defn move-taxi [taxi delta-lat delta-lng]
  (-> taxi
      (update :lng + delta-lng)
      (update :lat + delta-lat)))


(defn move-in-background [taxi duration]
 (future
   (dotimes [i duration]
     (Thread/sleep 2000)
     (swap! taxi move-taxi (rand) (rand)))))


(def taxis (atom {"A" (make-taxi "A")
                  "B" (make-taxi "B")}))

;; e.g.
;; (move-in-background  (@taxis "B") 60)
;; (move-in-background  (@taxis "A") 60)

(defn safe-parse-float [s]
  (ignore-errors (Double/parseDouble s)))

(defn get-taxi-detail [taxi-name]
  (deref (@taxis taxi-name))
  )

(defn get-nearest-taxis-from [taxis]
  (fn [lat lng distance]
    (let [distance-to (partial distance-between [lat lng])
          taxis (vals (deref taxis))
          selected (filter (fn [taxi]
                             (> distance (distance-to [(@taxi :lat ) (@taxi :lng)])))
                           taxis)]
      (map deref  selected))))


(def get-taxis (get-nearest-taxis-from taxis))
(defn update-taxi [taxi-name lat lng]
  (let [taxi (@taxis taxi-name)]
    (if (nil? taxi)
    taxi
    (swap! taxi move-taxi lat lng))))