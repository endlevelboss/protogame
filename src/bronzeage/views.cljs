(ns bronzeage.views
  (:require
   [re-frame.core :as re-frame]
   [bronzeage.hexmap :refer [hexagon-mapper]]
   [bronzeage.subs :as subs]))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [:h1
      "Hello from " @name]
     [hexagon-mapper]
     ]))

