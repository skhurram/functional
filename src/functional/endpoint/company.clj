(ns functional.endpoint.company
  (:require [compojure.core :refer :all]
            [functional.component.persistence :as persistence]
            [ring.util.response :as resp]
            [hiccup.core :refer [h]]
            [hiccup.page :as page]
            [hiccup.form :as form]
            [ring.util.anti-forgery :as anti-forgery]
            [clojure.string :as string])
  (:import (java.util Date)))

(defn common [title & body]
  (page/html5
    [:head
     [:meta {:charset "utf-8"}]
     [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
     [:meta {:name "viewport" :content
                   "width=device-width, initial-scale=1, maximum-scale=1"}]
     [:title title]
     (page/include-css "/stylesheets/base.css"
                       "/stylesheets/skeleton.css"
                       "/stylesheets/screen.css")
     (page/include-css "http://fonts.googleapis.com/css?family=Sigmar+One&v1")]
    [:body
     [:div {:id "header"}
      [:h1 {:class "container"} [:a {:href "/company"} "Companies"]]]
     [:div {:id "content" :class "container"} body]]))

(defn company-form [{:keys [id first_name last_name company
                            description email city country
                            website registration_date active] :as company}]
  [:div {:id "company-form" :class "sixteen columns alpha omega"}
   (form/form-to [:post "/company"]
                 (anti-forgery/anti-forgery-field)
                 [:div
                  (form/label "first_name" "First name")
                  (form/text-field "first_name" first_name)]
                 [:div
                  (form/label "last_name" "Last name")
                  (form/text-field "last_name" last_name)]
                 [:div
                  (form/label "company" "Company")
                  (form/text-field "company" company)]
                 [:div
                  (form/label "description" "Description")
                  (form/text-area "description" description)]
                 [:div
                  (form/label "email" "Email")
                  (form/text-field "email" email)]
                 [:div
                  (form/label "city" "City")
                  (form/text-field "city" city)]
                 [:div
                  (form/label "country" "Country")
                  (form/text-field "country" country)]
                 [:div
                  (form/label "website" "Website")
                  (form/text-field "website" website)]
                 [:div
                  (form/label "registration_date" "Registration date")
                  (form/text-field "registration_date" registration_date)]
                 [:div
                  (form/label "active" "Active")
                  (form/check-box "active" active)]
                 (form/hidden-field "id" id)

                 (form/submit-button "Save Company"))])

(defn display-companies [companies]
  [:div {:class "companies sixteen columns alpha omega"}
   (map
     (fn [company] [:h2 {:class "company"}
                    [:div
                     [:div
                      [:span (:first_name company) "&nbsp;"]
                      [:span (:last_name company) "&nbsp;"]
                      [:span
                       [:a {:href (str "/company/" (:id company))} (:company company) "&nbsp;"]]
                      [:span (:email company) "&nbsp;"]
                      [:span (:city company) "&nbsp;"]
                      [:span (:country company) "&nbsp;"]
                      [:span (:website company) "&nbsp;"]
                      [:span (:registration_date company) "&nbsp;"]
                      [:span (:active company) "&nbsp;"]
                      [:a {:href (str "/company/delete/" (:id company))} "Delete"]]
                     [:div (:description company) "&nbsp;"]]])
     companies)])

(defn index [companies company]
  (common "Companies"
          (company-form company)
          [:div {:class "clear"}]
          (display-companies companies)))

(defn checkbox-value [value]
  (if (string/blank? value)
    0
    (if (= (string/trim value) "true")
      1
      0)))

(defn company-endpoint [db]
  (let [pers (:persistence db)]
    (context "/company" []
     (GET "/" [] (index (persistence/get-companies {} pers) nil))
     (GET "/:id" [id] (index (persistence/get-companies {} pers) (first (persistence/get-company {:id id} pers))))
     (GET "/delete/:id" [id]
       (persistence/delete-company! {:id id} pers)
       (resp/redirect "/company"))
     (POST "/" [id first_name last_name company description email city country website registration_date active]
       (println "registration_date=" registration_date ".")
       (persistence/save
         {:id     id :first_name first_name :last_name last_name :company company :description description
          :email  email :city city :country country :website website :registration_date registration_date
          :active (checkbox-value active) :created_at (Date.) :updated_at (Date.)}
         pers)
       (resp/redirect "/company")))))


