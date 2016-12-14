(ns functional.endpoint.company-api
  (:require [functional.component.persistence :as persistence]
            [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s])
  (:import java.util.Date))

(s/defschema Company
             {(s/optional-key :id ) Integer
              (s/optional-key :first_name) String
              (s/optional-key :last_name) String
              (s/optional-key :company) String
              (s/optional-key :description) String
              (s/optional-key :email) String
              (s/optional-key :city) String
              (s/optional-key :country) String
              (s/optional-key :website) String
              (s/optional-key :registration_date) Date
              (s/optional-key :created_at) Date
              (s/optional-key :updated_at) Date
              (s/optional-key :active) Boolean})

(defn company-api-endpoint [config]
  (api
    {:swagger
     {:ui "/swagger"
      :spec "/swagger.json"
      :data {:info {:title "Company API"
                    :description "Functiona company api"}
             :tags [{:name "company", :description "employer"}]}}}

    (context "/api" []
              :tags ["companies"]

              (GET "/companies" []
                    :return  [Company]
                    :summary "returns the list of companies"
                    (ok (persistence/get-companies {} (:persistence config))))

              (GET "/company/:id" []
                    :return       Company
                    :path-params  [id :- String]
                    :summary      "returns the company with a given id"
                    (ok (first (persistence/get-company {:id id} (:persistence config)))))

              (POST "/company"    []
                     :return      Long
                     :body-params [company :- Company]
                     :summary     "creates a new company record."
                     (ok (persistence/save company (:persistence config))))

              (DELETE "/company/"    []
                       :return      Long
                       :body-params [id :- Long]
                       :summary     "deletes the company record with the given id."
                       (ok (persistence/delete-company! {:id id} (:persistence config)))))))