(ns full-stack-clj.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [full-stack-clj.events :as events]
   [full-stack-clj.routes :as routes]
   [full-stack-clj.views :as views]
   [full-stack-clj.config :as config]
   ))


(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-panel] root-el)))

(defn init []
  (routes/app-routes)
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))