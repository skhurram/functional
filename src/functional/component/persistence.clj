(ns functional.component.persistence
  (:require [com.stuartsierra.component :as component]
            [yesql.core :refer [defqueries]]))

(defqueries "functional/sql/queries.sql")

(defn save [{:keys [id] :as company} pers]
  (if id
    (update-company! company pers)
    (-> (create-company<! company pers) :generated_key)))

;; for web page
(defn save--- [{:keys [id] :as company} pers]
  (if (clojure.string/blank? id)
    (create-company<! company pers)
    (update-company! company pers)))

(defrecord Persistence [db]
  component/Lifecycle
  (start [component]
    (assoc component :connection (-> db :spec)))
  (stop [component]
    (dissoc component :connection)))

(defn persistence [db]
  (->Persistence db))

(comment
  (require '[clojure.java.jdbc :as j])

  (j/query (-> system :db :spec)
           ["select * from company_tbl" ])

  (j/query (-> system :persistence :connection)
           ["select * from company_tbl" ])


  (require '[reloaded.repl :refer [system]])

  (functional.component.persistence/get-companies {} (-> system :persistence))
  (get-companies {} (-> system :persistence))

  (get-company {:id 4} (-> system :persistence))

  (def c4 (get-company {:id 4} (-> system :persistence)))

  (def c-n (save {:description "string",
                          :email "string",
                          :first_name "string",
                          :city "string",
                          :updated_at #inst"2016-12-06T23:00:00.000000000-00:00",
                          :active true,
                          ; :id 4,
                          :website "string",
                          :last_name "string",
                          :registration_date #inst"2016-12-06T23:00:00.000-00:00",
                          :country "string",
                          :company "string",
                          :created_at #inst"2016-12-06T23:00:00.000000000-00:00"} (-> system :persistence)))

  (delete-company! {:id 2} (-> system :persistence))


  ;; id can also be specified but should not be
  (create-company<! {:id 2,
                    :title "Company B",
                    :city "Karachi",
                    :country "Pakistan",
                    :join_date #inst"2016-12-05T23:00:00.000-00:00"}
                   (-> system :persistence))

  (create-company<! {:title "Company B",
                    :city "Karachi",
                    :country "Pakistan",
                    :join_date #inst"2016-12-05T23:00:00.000-00:00"}
                   (-> system :persistence))


  )