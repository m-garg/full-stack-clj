(ns full-stack-clj.db)

(def default-db
  {:editor {:title {:value ""
                    :error-msg ""}
            :description {:value ""
                          :error-msg ""}}
   :tickets []
   :active-panel :home-panel})