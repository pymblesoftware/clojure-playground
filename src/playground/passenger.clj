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


