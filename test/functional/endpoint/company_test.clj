(ns functional.endpoint.company-test
  (:require [clojure.test :refer :all]
            [shrubbery.core :as shrub]
            [functional.endpoint.company :as company]))

(def handler
  (company/company-endpoint {}))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 1))))
