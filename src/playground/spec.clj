(ns playground.spec
  (:require [clojure.spec :as s]))


(s/def ::lat (s/and number? #(>= % -90) #(<= % 90)))
(s/def ::lng (s/and number? #(>= % -180) #(<= % 180)))
(s/def ::location (s/keys :req-un [::lat ::lng]))

(s/def ::passenger string?)

(s/def ::job (s/keys :req-un [::passenger ::location]))

(defn valid-passenger? [passenger]
  (print passenger)
  (s/valid? ::passenger passenger))

(defn valid-location? [location]
  (s/valid? ::location location))

(defn valid-job? [job]
  (s/valid? ::job job))

(defn valid-lat? [lat]
  (s/valid? ::lat lat))

(defn valid-lng? [lng]
  (s/valid? ::lng lng))
