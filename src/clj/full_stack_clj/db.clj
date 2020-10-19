(ns full-stack-clj.db
  (:require
   [monger.core :as mg]
   [monger.collection :as mc]))


(def conn (mg/connect {:host "shiva.local" :port 27017}))
(def db (mg/get-db conn "full-stack-clj-test"))


(defn insert-ticket [title description]
  (mc/insert-and-return db "tickets" {:title title :description description}))

(defn get-tickets []
  (mc/find-maps db "tickets"))

(defn delete-all-tickets [] (mc/remove db "tickets"))