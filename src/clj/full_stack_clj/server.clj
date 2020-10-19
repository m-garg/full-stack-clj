(ns full-stack-clj.server
  (:require [full-stack-clj.handler :refer [handler]]
            [config.core :refer [env]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.adapter.jetty :refer [run-jetty]])
  (:gen-class))

 (defn -main [& _args]
   (let [port (or (env :port) 3000)]
     (run-jetty (wrap-defaults handler api-defaults) {:port port :join? false})))
