(ns bartleby.db
  (:require [com.stuartsierra.component :as component]
            [hikari-cp.core :refer [make-datasource close-datasource]]
            [hugsql.core :as hugsql]
            [taoensso.timbre :refer [log]]))

(defn- make-pool-spec
  [host db user pass]
  {:jdbc-url (format "jdbc:postgresql://%s/%s" host db)
   :username user
   :password pass})

(defprotocol HasDataSource
  (get-datasource [this]))

(defrecord DBCon [host dbname username password cp]
  component/Lifecycle
  (start [component]
    (if-not cp
      (do
        (log :info "Connecting to db")
        (let [pool-spec (make-pool-spec host dbname username password)
              pool (make-datasource pool-spec)]
          (assoc component :cp pool)))
      component))
  (stop [component]
    (if cp
      (do
        (log :info "Dropping connection to db:" cp)
        (close-datasource cp)
        (assoc component :cp nil))
      component))
  HasDataSource
  (get-datasource [component]
    {:datasource cp}))

(defn connection
  [config]
  (map->DBCon {:host (:host config)
               :username (:username config)
               :password (:password config)
               :dbname (:name config)}))

(hugsql/def-db-fns "sql/queries.sql")
  
    
