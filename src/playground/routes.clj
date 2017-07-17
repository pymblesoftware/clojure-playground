(ns playground.routes
  (:require [compojure.core :refer :all]
            [ring.middleware.json :refer :all]
            [ring.middleware.defaults :refer :all]
            [ring.util.response :refer [response ]]
            [gocatch.subsystems.utils :refer [ignore-errors]]
            [playground.job :as job]
            [playground.taxis :as taxis]))

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

(defroutes raw-routes
  (ANY  "/debug" [] (wrap-defaults debug-handler (dissoc site-defaults :security)))
  (ANY  "/" [foo] (foo-handler foo))
  (context "/taxis" [] taxis/taxi-routes)
  (context "/jobs" [] job/job-routes))

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
