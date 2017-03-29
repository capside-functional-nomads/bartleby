(ns bartleby.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [bartleby.events]))

#_(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn main-view []
  [:div
   [:h1 "Todo app"]
   [:p "Bartleby the scrivener, you know"]])

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render main-view
                  (.getElementById js/document "app")))

(defn ^:export init[]
  (re-frame/dispatch-sync [:initialize-db])
  #_(dev-setup)
  (mount-root))

(defn ^:export load-tasks[]
  (re-frame/dispatch [:load-tasks]))
