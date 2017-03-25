(ns bartleby.handler)

(defn app
  [req]
  {:status 200
   :headers {"Content-type" "text/plain"}
   :body "ok"})
