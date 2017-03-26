(ns bartleby.db.migrations
  (:require [migratus.core :as migratus]
            [com.stuartsierra.component :as component]
            [taoensso.timbre :refer [log]]))

(defrecord Migrator [host dbname username password migrations-dir]
  component/Lifecycle
  (start [component]
    (log :info "Performing DB migrations")
    (let [config {:store :database
                  :migration-dir migrations-dir
                  :db {:classname "org.postgresql.Driver"
                       :subprotocol "postgresql"
                       :subname (format "//%s/%s" host dbname)
                       :username username
                       :password password}}]
      (migratus/migrate config)
      (log :info "Migrations completed")
      component))
  (stop [component]
    component))

(defn migrator
  [config]
  (map->Migrator {:host (:host config)
                  :dbname (:name config)
                  :username (:username config)
                  :password (:password config)
                  :migrations-dir (:migrations-dir config)}))
 
(defn- config->migratus-params
  [config]
  {:store :database
   :migration-dir (:migrations-dir config)
   :db {:classname "org.postgresql.Driver"
        :subprotocol "postgresql"
        :subname (format "//%s/%s" (:host config) (:name config))
        :username (:username config)
        :password (:password config)}})

(defn migrate!
  [config]
  (migratus/migrate (config->migratus-params config)))

(defn rollback!
  [config]
  (migratus/rollback (config->migratus-params config)))

(defn pending-list
  [config]
  (migratus/pending-list (config->migratus-params config)))

(defn debug-config
  [config]
  (config->migratus-params config))

(defn create!
  [config migration-name]
  (migratus/create (config->migratus-params config) migration-name))
