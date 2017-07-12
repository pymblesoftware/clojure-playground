(ns playground.routes
  (:require [compojure.core :refer :all]
            [ring.middleware.json :refer :all]
            [ring.middleware.defaults :refer :all]
            [ring.util.response :refer [response ]]
            [playground.taxis :refer [get-taxis update-taxi get-taxi-detail]]
            [gocatch.subsystems.utils :refer [ignore-errors]]))

(defn debug-handler [req]
  (clojure.pprint/pprint req)

  {:status 200
   :body {:some 7 :blah "your body"}})

(defn dummy-mw
  [handler]

  (fn [req]
    (let [r (handler (assoc  req :example  (java.util.Date.)))
          body (:body r)]
      (assoc r
             :body (str "New body is " body)))))

(defn foo-handler [x]
  (println "They said " x)
  {:status 200})


(def max-distance
  "The max distance, in meters, which a client is allowed to see a taxi"
  500)

(defn safe-parse-float [s]
  (ignore-errors (Double/parseDouble s)))


(defn get-taxis-handler [lat lng]
  (let [lat (safe-parse-float lat)
        lng (safe-parse-float lng)]
    (if (and lat lng)
      (response {:taxis (get-taxis lat lng max-distance)})
      {:status 400
       :body "Invalid lat/lng supplied"})))

(defn update-taxi-handler [taxi-name {:keys [lat lng]}]
  ;; put some clojure.spec magic in here

  (if (and lat lng)
    (if-let [new-taxi (update-taxi taxi-name lat lng)]
      (response {:taxi new-taxi})
      {:status 404
       :body "Taxi not found.\n"})
    {:status 400
     :body "Invalid location.\n"}))
(defn get-taxi-detail-handler [taxi-name]
  (let [taxi (get-taxi-detail taxi-name)]
    (println taxi)
    (if taxi
      (response {:taxi taxi})
      {:status 404
       :body "Taxi not found.\n"})))

(defroutes raw-routes
  (ANY  "/debug" [] (wrap-defaults debug-handler (dissoc site-defaults :security)))
  (ANY  "/" [foo] (foo-handler foo))
  (GET "/taxis" [lat lng] (get-taxis-handler lat lng))
  (PUT "/taxis/:t-name" [t-name location] (update-taxi-handler t-name location))
  (GET "/taxis/:t-name" [t-name ] (get-taxi-detail-handler t-name)))


(def example-routes
  (-> raw-routes
      (wrap-defaults (dissoc site-defaults :security))
      (ring.middleware.json/wrap-json-params)
      (ring.middleware.json/wrap-json-response)))

;;   drivers constantly updating their locations by
;;   PUT /taxis/:name
;;      response should be:
;;      200 OK "" if nothing to do
;;      200 ok {:jobs-available [{:passenger "bob" :lat 1 :lng 2]}
;;
;;   passenger posts a new job:
;;   POST /job/
;;      params of the post {:passegner -name  current location}
