(ns bartleby.core
  (:require [taoensso.timbre :refer [log]]
            [com.stuartsierra.component :as component]
            [cprop.core :refer [load-config]]
            [bartleby.db.migrations :as migrations]
            [bartleby.server :as server]
            [bartleby.db :as db]
            [clojure.tools.namespace.repl :refer [refresh]]))

              
(defn new-system
  [& opts]
  (let [config (merge (load-config) (or opts {}))
        prod? (:prod? config)]
    (component/system-map
     :migrator (migrations/migrator (:migrator config))
     :db (db/connection (:db config))
     :web (component/using
           (server/make prod?)
           [:db])
     :all (component/using {} [:web :migrator]))))

(def system nil)

(defn stop
  []
  (when-not (nil? system)
    (alter-var-root #'system component/stop)))

(defn start
  []
  (when-not (nil? system)
    (alter-var-root #'system component/start)))

(defn init
  []
  (stop)
  (alter-var-root #'system (fn [_] (new-system))))

(defn go
  []
  (init)
  (start))

(defn reset
  []
  (refresh :after 'bartleby.core/go))

(defn schedule-shutdown!
  [sys]
  (letfn [(stop-and-log []
            (log :info "Stopping system")
            (stop)
            (log :info "System stopped"))]
    (Thread. stop-and-log)))


(defn -main 
  [& args]
  (let [sys (new-system)]
    (.addShutdownHook (Runtime/getRuntime) (schedule-shutdown! sys))
    (log :info "Starting system")
    (go)
    (log :info "System started")))
