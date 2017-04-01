(ns bartleby.handler
  (:require [taoensso.timbre :refer [log]]
            [ring.util.http-response :as http-resp]
            [ring.util.response :as resp]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [bartleby.db :as db]))

(defn other-implementation-of-status
  []
  {:status 200
   :headers {"Content-type" "text/plain"}
   :body "api-ok"})

(defn api
  [db-con]
  (routes
   (GET "/status" []
     (http-resp/ok {:status :ok}))
   (GET "/tasks/" []
     (let [tasks (db/find-incomplete-tasks db-con)]
       (http-resp/ok tasks)))
   (GET "/tasks/:id" [id]
     (http-resp/not-implemented "not implemented"))
   (POST "/tasks/" []
     (http-resp/not-implemented "not implemented"))
   (DELETE "/tasks/:id" [id]
     (http-resp/not-implemented "not implemented"))
   (PATCH "/tasks/:id" [id]
     (http-resp/not-implemented "not implemented"))))

(defn entry-point
  [db-con]
  (routes
   (GET "/" []
     (resp/resource-response "index.html" {:root "public"}))
   (-> (api db-con)
       (wrap-json-body {:keywords? true :bigdecimals? true})
       wrap-json-response)
   (route/resources "/" {:root "public"})
   (route/not-found "404 Not found")))
