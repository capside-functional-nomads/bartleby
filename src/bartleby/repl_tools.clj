(ns bartleby.repl-tools
  (:require [cprop.core :refer [load-config]]
            [bartleby.db.migrations :as migrations]))


(defn db-migrate!
  []
  (let [config (load-config)]
    (migrations/migrate! (:migrations config))))

(defn db-rollback!
  []
  (let [config (load-config)]
    (migrations/rollback! (:migrations config))))

(defn create-db-migration!
  [name]
  (let [config (load-config)]
    (migrations/create! (:migrations config) name)))

(defn get-pending-migrations
  []
  (let [config (load-config)]
    (migrations/pending-list (:migrations config))))
