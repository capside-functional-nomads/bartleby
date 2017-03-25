(ns bartleby.db.migrations
  (:require [migratus.core :as migratus]))

(defn- config->migratus-params
  [config]
  {:store :database
   :migration-dir (:migrations-dir config)
   :db {:classname "org.postgresql.Driver"
        :subprotocol "postgresql"
        :subname (format "//%s/%s" (:db-host config) (:db-name config))
        :user (:db-user config)
        :password (:db-password config)}})
  
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
