(ns full-stack-clj.handler
  (:require
   [compojure.core :refer [GET POST defroutes]]
   [compojure.route :refer [resources not-found]]
   [full-stack-clj.db :as database]
   [ring.middleware.reload :refer [wrap-reload]]
   [jumblerg.middleware.cors :refer [wrap-cors]]
   [shadow.http.push-state :as push-state]
   [cheshire.core :as cc]))

(defn get-response [data]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (cc/generate-string data)})

(defn _id->str [inp] (map (fn [item] (dissoc (assoc item :id (str (item :_id))) :_id)) inp))

(defn get-tickets [_]
  (println (database/get-tickets))
  (println (_id->str (database/get-tickets)))
  (get-response (_id->str (database/get-tickets))))


(defn submit-ticket [req]
  (let [params (:params req)
        title (:title params)
        description (:description params)]
    (println "submit ticket called " title description)
    (database/insert-ticket title description)
    (get-response {:text "success"})))

(defroutes routes
  ; (GET "/" [] (resource-response "index.html" {:root "public"}))
  (GET "/tickets" [] get-tickets)
  (POST "/ticket" [] submit-ticket)
  (not-found "404 route not found...")
  ; (resources "/")
  )

(def dev-handler (-> #'routes wrap-reload push-state/handle))

(def handler (wrap-cors routes #".*"))
