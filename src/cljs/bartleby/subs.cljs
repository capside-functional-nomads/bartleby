(ns bartleby.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 :tasks
 (fn [db _]
   (:tasks db)))

(re-frame/reg-sub
 :rest
 (fn [db _]
   (:rest db)))

