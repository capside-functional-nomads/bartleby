(ns bartleby.server
  (:require [immutant.web :as server]
            [taoensso.timbre :as timbre :refer [log]]
            [cprop.core :refer [load-config]]
            [ring.logger :as logger]
            [ring.middleware.stacktrace :as tr]
            [bartleby.db.migrations :as migrations]
            [com.stuartsierra.component :as component]
            [bartleby.db :as db]
            [bartleby.handler :as handler]))


(defn start-devel!
  [component app]
  (log :info "Starting app server: development mode")
  (assoc component :server (server/run (tr/wrap-stacktrace app))))

(defn start-prod!
  [component app]
  (log :info "Starting app server: production mode")
  (assoc component :server (server/run app)))

(defrecord WebServer [prod? db server]
  component/Lifecycle
  (start [component]
    (when (nil? (:server component))
      (let [app (-> (handler/entry-point (db/get-datasource db))
                    logger/wrap-with-logger)]
        (if prod?
          (start-prod! component app)
          (start-devel! component app)))))
  (stop [component]
    (when-not (nil? (:server component))
      (server/stop (:server component))
      (assoc component :server nil))))


(defn make 
  [prod?]
  (map->WebServer {:prod? prod?}))
 
