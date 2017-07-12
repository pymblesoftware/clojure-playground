(ns playground.taxis-test
  (:require [playground.taxis :refer :all]
            [clojure.test :refer :all]))



(deftest eg-1
  (is (= 2 (+ 1 1)))
  (is (= 2 (+ 1 1)) "Can't add 1 and 1!!")
  (is (= 2 (+ 1 1))))

(deftest eg-2
  (let [tx @(make-taxi "foo")]
    (is (= (move-taxi tx 1 1)
           {:name "foo" :lat 1 :lng 1}))))


(deftest get-nearest-taixs-from-test
  (let [test-taxis (atom {"a" (make-taxi "a")
                          "b" (make-taxi "b")})
        taxis-a (swap! (@test-taxis "a") move-taxi 1 1)
        nearest-taxis ((get-nearest-taxis-from test-taxis) 1 1 10)]
    (is (= taxis-a {:name "a" :lat 1 :lng 1}))
    (is (= nearest-taxis
           (list taxis-a)))))
