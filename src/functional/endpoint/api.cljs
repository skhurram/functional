(ns functional.endpoint.api
  (:require-macros [adzerk.env :as env]
                   [cljs.core.async.macros :refer [go]])
  (:require [ajax.core :refer [GET POST PUT DELETE]]
            [cljs.core.async :refer [put! chan <!]]
            [cljs-time.core :as time]
            [cljs-time.format :as format]))

;(def API-URL "https://blooming-fjord-58734.herokuapp.com")
;(def API-URL "http://localhost:3000")

(env/def
  API-URL "https://blooming-fjord-58734.herokuapp.com")

(def custom-formatter (format/formatter "yyyy-MM-ddTHH:mm:ssZ"))

(defn call
  ([method url]
   (call method url {}))
  ([method url body]
   (let [ch (chan)]
     (method (str API-URL url) (merge {:format          :json
                                        :response-format :json
                                        :keywords?       true
                                        :handler         #(put! ch %)}
                                       body))
     ch)))

(defn get-companies
  []
  (call GET "/api/companies"))

(defn get-company
  [id]
  (call GET (str "/api/company/" id)))

(defn save-company
  [company]
  :description :updated_at :active :registration_date :created_at

  ;; for brewity these fields are not yet exposed in the UI
  ;; :description :updated_at :active :registration_date :created_at
  (call POST "/api/company"
        {:params {:company
                  (assoc company :description ""
                                 :updated_at (format/unparse custom-formatter (time/time-now))
                                 :active true
                                 :registration_date (format/unparse custom-formatter (time/time-now))
                                 :created_at (format/unparse custom-formatter (time/time-now)))}})) ;; timestamps are faked for now

(defn delete-company
  [id]
  (call DELETE (str "/api/company/" id)))


;; FIXME on creation of new company:
;; - optimistically add the new company in the app-state
;; - send the data to db and receive the new id
;; - update id in the :companies-list for that company
;;
;; Best is to generate a uuid and send it along to backend.
;; Return the new db id from the backend. Have a handler on the
;; frontend to replace the uuid with this new db id. David Nolen's
;; idea of temp-id.
