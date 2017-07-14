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


(defn format-passenger [name]
  "Format output with the name of the passenger that was found for the job"
  {
   :passenger (   (deref  (get (deref playground.passenger/passengers) name  )  )  :name  )
   :lat ( (deref  (get (deref playground.passenger/passengers) name  )  )  :lat  )
   :lng ( (deref  (get (deref playground.passenger/passengers) name  )  )  :lng  )
   }
  )

;;
;;
;;

(defn match-passenger-taxi [pass]
  ;(do
    (println (str "examining:" (deref pass)  ) )
    (if
      (= (( deref pass  ) :name) "Bob")
        (do (println "found")
            pass )                                                    ; Want this one.
          nil)                                                     ; else nil.
        )
    ;)



;(defn strategy [lat lng]
;  (for [[k v] (deref playground.passenger/passengers)]
;    ;(prn k v))
;    (:when (not= nil (match-passenger-taxi v)) v)  v))

(defn strategy [lat lng]
  (doseq [[k v] (deref playground.passenger/passengers)]
    ;(prn k v))
   (:when (not= nil (match-passenger-taxi v)) v)  v))

;(defn strategy2 [lat lng]
;   (for ([k v] (deref playground.passenger/passengers)) (match-passenger-taxi v)))

;; TODO: --- finish this function to find passengers near taxis.

;(defn strategy3 [lat lng]
;  (   (deref playground.passenger/passengers)))

(defn new-matcher [pass]
  (println (str "new-matcher pass:" pass) )
  pass
  )

(defn strategy5 [lat lng]
  ;(println (str ":" ))
  ;(filter new-matcher (deref passengers))
  (let temp [(deref passengers)]  )
  (filter match-passenger-taxi (deref passengers))

  )

(defn strategy4 [lat lng]
  (first (deref playground.passenger/passengers)))

(defn find-passenger-for-job [lat lng]
  (first (strategy4 lat lng) ))


(defn get-jobs-avail-internal  [lat lng]
  (format-passenger (find-passenger-for-job lat lng)))
