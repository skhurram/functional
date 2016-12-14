(ns functional.endpoint.company-api-test
  (:require [clojure.test :refer :all]
            [shrubbery.core :as shrub]
            [functional.endpoint.company-api :as company-api]))

(def handler
  (company-api/company-api-endpoint {}))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 1))))
