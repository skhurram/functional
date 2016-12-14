(ns functional.endpoint.company
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
            [functional.endpoint.api :as api]
            [functional.util :as util]
            ;[functional.endpoint.company-table :as table]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [om-tools.core :refer-macros [defcomponent]]
            [cljs.core.async :refer [<!]]
            [secretary.core :as sec :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as EventType])
  (:import goog.History))

(enable-console-print!)

(defonce app-state (atom {:companies-list []
                          ; :edit-id 4
                          :edit-company nil}))

(sec/set-config! :prefix "#")

(defcomponent top-nav [data]
  (render [_]
    (html
      [:nav
       [:div {:class "nav-wrapper"}
        [:a {:href "#", :class "brand-logo"} "Functional Company Registration"]
        [:ul {:id "nav-mobile", :class "right hide-on-med-and-down"}
         [:li
          [:a {:href "https://gitlab.com/skhurram/functional"} "GitLab"]]]]])))


(defn load-company [data company-id]
  ;; we don't need to load from the db
  (go (let [edit-company (<! (api/get-company company-id))]
        (om/update! data :edit-company edit-company)))

  ;(om/update! data :edit-company edit-company)
  )

(defn save-company [{:keys [edit-company] :as data}]
  (om/transact! data [:companies-list (keyword (-> edit-company :id str keyword))] (fn [_] edit-company))
  (api/save-company edit-company))

(defn table-view [data owner]
  (reify
    om/IWillMount
    (will-mount [_]
      (go (let [comps (<! (api/get-companies))]
            (om/update! data :companies-list (into {} (map (fn [c1] {(-> c1 :id str keyword) c1}) comps))))))
    om/IRenderState
    (render-state
      [_ state]
      (let [columns [:first_name :last_name :company :email :website :city :country] ;; :active registration_date - later
            rows (-> data :companies-list vals)]
        (html
          [:div {:class "row"}
           [:div {:class "card z-depth-5"}
            [:table {:class "bordered striped highlight"}
             [:thead
              [:tr
               (for [[idx header] (map-indexed vector columns)]
                 [:th {:key idx} (name header)])]]
             [:tbody
              (for [row rows]
                [:tr {:key (:id row) :on-click (fn [_]
                                                (load-company data (:id row)))}
                 (for [[idx column] (map-indexed vector columns)]
                   [:td {:key   idx}
                    (get row column)])])]]]])))))

(defn handle-change [e data edit-key owner]
  (om/transact! data edit-key (fn [_] (.. e -target -value))))

(defcomponent company-form [data owner]
  #_(will-mount [_]
    (go (let [edit-company (<! (api/get-company (-> data :edit-id)))]
          (om/update! data :edit-company edit-company))))
  (render-state [_ _]
    (html
      (let [{:keys [first_name last_name company email website city country]} (-> data :edit-company)]
        [:div {:class "row"}
         [:form {:class "card z-depth-5 col s12"}
          [:div {:class "row"}
           [:div {:class "input-field col s6 active"}
            [:input {:id "first_name", :type "text", :class "validate" :value first_name :placeholder "Contact first name"
                     :on-change #(handle-change % data [:edit-company :first_name]  owner)}]
            [:label {:class "active" :for "first_name"} "First Name"]]
           [:div {:class "input-field col s6"}
            [:input {:id "last_name", :type "text", :class "validate" :value last_name :placeholder "Contact last name"
                     :on-change #(handle-change % data [:edit-company :last_name]  owner)}]
            [:label {:for "last_name" :class "active"} "Last Name"]]]
          [:div {:class "row"}
           [:div {:class "input-field col s12"}
            [:input {:id "company", :type "text", :class "validate" :value company :placeholder "Company name"
                     :on-change #(handle-change % data [:edit-company :company]  owner)}]
            [:label {:class "active" :for "company"} "Company"]]]
          [:div {:class "row"}
           [:div {:class "input-field col s6"}
            [:input {:id "email", :type "email", :class "validate" :value email :placeholder "Contact email"
                     :on-change #(handle-change % data [:edit-company :email]  owner)}]
            [:label {:class "active" :for "email"} "Email"]]
           [:div {:class "input-field col s6"}
            [:input {:id "website", :type "text", :class "validate" :value website :placeholder "Company website"
                     :on-change #(handle-change % data [:edit-company :website]  owner)}]
            [:label {:class "active" :for "website"} "Website"]]]
          [:div {:class "row"}
           [:div {:class "input-field col s6"}
            [:input {:id "city", :type "text", :class "validate" :value city :placeholder "City"
                     :on-change #(handle-change % data [:edit-company :city]  owner)}]
            [:label {:class "active" :for "city"} "City"]]
           [:div {:class "input-field col s6"}
            [:input {:id "Country", :type "text", :class "validate" :value country :placeholder "Country"
                     :on-change #(handle-change % data [:edit-company :country]  owner)}]
            [:label {:class "active" :for "Country"} "Country"]]]
          ;;[:input {:id "date", :type "date", :class "datepicker"}]
          [:div {:class "row"}
           [:div {:class "input-field col s8"}
            [:a {:class "waves-effect waves-light btn"} "Cancel"]
            [:span " "]
            [:a {:class "waves-effect waves-light btn"
                 :on-click (fn [_]
                             (save-company data))} "Save"]]]]]))))

(defcomponent main-view [app-state]
  (render [_]
    (html
      [:div
       (om/build top-nav app-state)
       [:div {:class "container"}
        (om/build table-view app-state)
        (om/build company-form app-state)]])))

(om/root
  main-view
  app-state
  {:target (. js/document (getElementById "app"))})