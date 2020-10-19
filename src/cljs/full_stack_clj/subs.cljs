(ns full-stack-clj.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::active-panel
 (fn [db _]
   (:active-panel db)))

(re-frame/reg-sub
 ::editor
 (fn [db]
   (:editor db)))

(re-frame/reg-sub
 ::tickets
 (fn [db]
   (:tickets db)))