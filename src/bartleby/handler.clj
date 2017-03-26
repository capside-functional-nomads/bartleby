(ns bartleby.handler
  (:require [ring.middleware.resource :refer [wrap-resource]]))

(defn ok-app
  [req]
  {:status 200
   :headers {"Content-type" "text/plain"}
   :body "ok"})

(def app (-> ok-app
             (wrap-resource "public")))
