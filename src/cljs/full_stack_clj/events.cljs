(ns full-stack-clj.events
  (:require
   [re-frame.core :as re-frame]
   [full-stack-clj.db :as db]
   [clojure.string :as string]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [day8.re-frame.http-fx]
   [ajax.core :as ajax]))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
            db/default-db))

(re-frame/reg-event-db
 ::set-active-panel
 (fn-traced [db [_ active-panel]]
            (assoc db :active-panel active-panel)))

(re-frame/reg-event-fx
 ::load-tickets-panel
 (fn-traced [coeff]
            (let [db (:db coeff)]
              {:db (assoc db :active-panel :tickets-panel)
               :http-xhrio {:method :get
                            :uri "http://127.0.0.1:3000/tickets"
                            :response-format (ajax/json-response-format {:keywords? true})
                            :on-success [::update-tickets]}})))

(re-frame/reg-event-db
 ::update-tickets
 (fn-traced [db [_ response]]
            (assoc db :tickets response)))

(re-frame/reg-event-db
 ::title-change
 (fn-traced [db [_ value]]
            (assoc-in db [:editor :title] {:value value :error-msg ""})))

(re-frame/reg-event-db
 ::description-change
 (fn-traced [db [_ value]]
            (assoc-in db [:editor :description] {:value value :error-msg ""})))

(re-frame/reg-event-db
 ::submit-ticket-success
 (fn-traced [db]
            (assoc db :active-panel :home-panel)))


(re-frame/reg-event-fx
 ::submit-ticket
 (fn-traced [coeff]
            (let [db (:db coeff)
                  title-val (string/trim (get-in db [:editor :title :value]))
                  description-val (string/trim (get-in db [:editor :description :value]))]
              (js/console.log (assoc-in db [:editor :title :error-msg] "Please enter a valid title."))
              (if
               (and (not (seq title-val)) (not (seq description-val)))
                {:db (->
                      (assoc-in db [:editor :title :error-msg] "Please enter a valid title.")
                      (assoc-in [:editor :description :error-msg] "Please enter a valid description."))}
                {:db db
                 :http-xhrio {:method :post
                              :uri "http://127.0.0.1:3000/ticket"
                              :format          (ajax/url-request-format)
                              :response-format (ajax/json-response-format {:keywords? true})
                              :params {:title (get-in db [:editor :title :value])
                                       :description (get-in db [:editor :description :value])}
                              :on-success [::submit-ticket-success]}}))))
