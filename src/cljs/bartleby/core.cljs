(ns bartleby.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]))

#_(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn main-view []
  [:div
   [:h1 "Todo app"]])

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render main-view
                  (.getElementById js/document "app")))

(defn ^:export init[]
  #_(re-frame/dispatch-sync [:initialize-db])
  #_(dev-setup)
  (mount-root))
