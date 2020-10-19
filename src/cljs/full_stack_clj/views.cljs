(ns full-stack-clj.views
  (:require
   [re-frame.core :as re-frame]
   [full-stack-clj.subs :as subs]
   [full-stack-clj.events :as events]))

(defn header []
  [:div
   [:a {:href "#/"}
    "'Home' "]
   [:a {:href "#/new"}
    "'Submit new ticket' "]
   [:a {:href "#/list"}
    "'See existing issues'"]])

(defn home-panel []
  [:div
   [:h1 (str "Welcome to Issue Tracker")]
   [:div
    [:a {:href "#/new"}
     "Submit new ticket"]]
   [:div
    [:a {:href "#/list"}
     "See existing issues"]]])


(defn editor-panel []
  (let [editor (re-frame/subscribe [::subs/editor])
        title (:title @editor)
        description (:description @editor)]
    [:form {:on-submit #(do (-> % .-preventDefault) (re-frame/dispatch [::events/submit-ticket]))}
     [:div
      [header]
      [:div
       [:h3 "Title"]
       [:input {:value (:value title)
                :placeholder "Please enter the title"
                :on-change #(re-frame/dispatch [::events/title-change (-> % .-target .-value)])}]
       [:div (:error-msg title)]]
      [:div
       [:h3 "Description"]
       [:textarea {:value (:value description)
                   :placeholder "Please enter the description"
                   :on-change #(re-frame/dispatch [::events/description-change (-> % .-target .-value)])}]
       [:div (:error-msg description)]]
      [:div
       [:button {:type "submit"}
        "Submit"]]]]))

(defn tickets-panel []
  (let [tickets (re-frame/subscribe [::subs/tickets])]
    [:div
     [header]
     (map-indexed (fn [index ticket] [:div {:key (:id ticket)} (str (inc index) ": " (:title ticket) " -- " (:description ticket))]) @tickets)]))


(defn- panels [panel-name]
  (case panel-name
    :home-panel [home-panel]
    :editor-panel [editor-panel]
    :tickets-panel [tickets-panel]
    [:div]))


(defn show-panel [panel-name]
  [panels panel-name])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    [show-panel @active-panel]))
