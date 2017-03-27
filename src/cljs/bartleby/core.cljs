(ns bartleby.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]))

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

(defn ^:export get-tasks []
  (go (let [resp (<! (http/get "http://localhost:8080/tasks/" {:with-credentials? false}))]
        (.info js/console (:status resp))
        (.info js/console "Tasks:\n")
        (.info js/console (clojure.string/join "\n" (map (fn [task] (str (:description task))) (:body resp)))))))
