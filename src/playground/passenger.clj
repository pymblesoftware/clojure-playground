(ns playground.passenger)


(defn make-passenger [name new-lat new-lng]
  (atom {:name name
         :lat  new-lat
         :lng new-lng}))


(def passengers (atom {"Bob" (make-passenger "Bob" 0 0)
                  "Bill" (make-passenger "Bill" 0 0)}))



(defn new-passenger! [name new-lat new-lng]
    (swap!  passengers conj {name (make-passenger name new-lat new-lng)}))


; Clear passenger data
(defn clear-passengers! []
  (swap! passengers {} )
  )


;(defn get-jobs-avail  []
;  [{:passenger "bob" :lat 1 :lng 2  }] )

(defn format-passenger [name]
  "Format output with the name of the passenger that was found for the job"
  {
   :passenger (   (deref  (get (deref playground.passenger/passengers) name  )  )  :name  )
   :lat ( (deref  (get (deref playground.passenger/passengers) name  )  )  :lat  )
   :lng ( (deref  (get (deref playground.passenger/passengers) name  )  )  :lng  )
   }
  )

(defn find-passenger-for-job [lat lng]
    "Bill"
  )


(defn get-jobs-avail-internal  [lat lng]
  (format-passenger (find-passenger-for-job lat lng)))
