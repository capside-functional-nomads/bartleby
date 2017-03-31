(ns bartleby.events
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [re-frame.core :as re-frame]
            [bartleby.db :as db]
            [cljs-http.client :as http]
            [ajax.core :as ajax]
            [day8.re-frame.http-fx]
            [cljs.core.async :refer [<!]]))

(re-frame/reg-event-db
 :initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-fx
 :load-tasks
 (fn [{:keys [db]} _]
   {:db (assoc db :rest "in progress")
    :http-xhrio {:method :get
                 :uri "/tasks/"
                 :timeout 5000
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [:http-req-ok]
                 :on-failure [:http-req-error]}}))

(re-frame/reg-event-db
 :http-req-ok
 (fn [db [_ result]]
   (merge db {:tasks result
              :rest "ok"})))

(re-frame/reg-event-db
 :http-req-error
 (fn [db [_ result]]
   (merge db {:rest "error"})))

(re-frame/reg-event-db
 :task-toggle-done
 (fn [db [_ id done]]
   (assoc db :tasks (map (fn [task]
                           (if (= (:id task) id)
                             {:id id :description (:description task) :done done}
                             task))
                         (:tasks db)))))
