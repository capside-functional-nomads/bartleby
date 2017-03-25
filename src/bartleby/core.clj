(ns bartleby.core
  (:require [immutant.web :as server]
            [taoensso.timbre :as timbre :refer [log]]
            [cprop.core :refer [load-config]]
            [ring.logger :as logger]
            [bartleby.db.migrations :as migrations]
            [bartleby.handler :refer [app]])
  (:gen-class))

(def entry-point (logger/wrap-with-logger app))

(defn start-production-server!
  [state config]
  (timbre/log :info "Starting server in production mode")
  (swap! state (server/run #'entry-point)))

(defn start-development-server!
  [state config]
  (timbre/log :info "Performing db migrations")
  (migrations/migrate! (:migrations config))
  (timbre/log :info "Starting server in development mode")
  (swap! state (server/run-dmc #'entry-point)))

(defn start-server!
  [state config]
  (let [prod? (:prod config)]
    (if prod?
      (start-production-server! state config)
      (start-development-server! state config))))

(defn schedule-shutdown!
  [state]
  (letfn [(stop-server! []
            (log :info "Stopping server")
            (swap! state server/stop)
            (log :info "Server stopped"))]
    (Thread. stop-server!)))

(defn -main 
  [& args]
  (let [the-server (atom nil)
        config (load-config)]
    (.addShutdownHook (Runtime/getRuntime) (schedule-shutdown! the-server))
    (start-server! the-server config)))
