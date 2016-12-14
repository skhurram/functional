(ns functional.endpoint.company-table
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
            [functional.endpoint.api :as api]
            [functional.util :as util]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [om-tools.core :refer-macros [defcomponent]]
            [cljs.core.async :refer [<!]]))

(enable-console-print!)

(defn table-view [data owner]
  (reify
    om/IWillMount
    (will-mount [_]
               (go (let [comps (<! (api/get-companies))]
                     (om/update! data :companies-list comps))))
    om/IRenderState
    (render-state
      [_ state]
      (let [columns [:first_name :last_name :company :email :website :city :country :active]
            rows (-> data :companies-list)]
        (html [:table
              [:thead
               [:tr
                (for [[idx header] (map-indexed vector columns)]
                  [:td {:key idx} (name header)])]]
               [:tbody
                (for [row rows]
                  [:tr {:key (:id row)}
                   (for [[idx column] (map-indexed vector columns)]
                     [:td {:key   idx}
                      (get row column)])])]])))))