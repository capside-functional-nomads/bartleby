(ns bartleby.events
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [re-frame.core :as re-frame]
            [bartleby.db :as db]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]))

(defn ^:export get-tasks []
  (go (let [resp (<! (http/get "http://localhost:8080/tasks/" {:with-credentials? false}))
            status (:status resp)]
        (.info js/console status)
        (when (= 200 status)
          (re-frame/dispatch [:tasks-loaded (:body resp)])
          (.info js/console "Tasks:\n")
          (.info js/console (clojure.string/join "\n" (map (fn [task] (str (:description task))) (:body resp))))))))

(re-frame/reg-event-db
 :initialize-db
 (fn [_ _]
   (.info js/console "initialize-db")
   db/default-db))

(re-frame/reg-event-db
 :load-tasks
 (fn [db [_ _]]
   (.info js/console "load-tasks")
   (let [new-db (assoc db :loading true)]
     (get-tasks)
     new-db)))

(re-frame/reg-event-db
 :tasks-loaded
 (fn [db [_ tasks]]
   (.info js/console "tasks loaded")
   (assoc db :tasks tasks)))

(re-frame/reg-event-db
 :task-toggle-done
 (fn [db [_ id done]]
   (assoc db :tasks (map (fn [task]
                           (if (= (:id task) id)
                             {:id id :description (:description task) :done done}
                             task))
                         (:tasks db)))))
