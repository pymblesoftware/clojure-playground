(ns playground.job-manager
  (:require [gocatch.subsystems.utils :refer [distance-between]]
            [playground.constants :refer [MAX-DISTANCE]]
            [playground.taxis :refer [get-taxis]]
            [playground.job :refer [make-job]]))

(def jobs (atom {}))
(def nearby-taxis (atom {}))

(defn register-job! [id job]
  (swap! jobs conj {id job})
  (loop [taxis (get-taxis (@job :lat) (@job :lng) MAX-DISTANCE)
         taxi-dict]))

(defn register-job! [id job]
  (swap! jobs conj {id job})
  (swap! nearby-taxis conj {id (atom {})})
  (let [taxis (get-taxis (@job :lat) (@job :lng) MAX-DISTANCE)
        taxi-map (reduce (fn [available-taxis part]
                  (assoc available-taxis (@part :name) @part))
                {} 
                taxis)]
    (swap! (@nearby-taxis id) conj {id (atom taxi-map)})
    ))
