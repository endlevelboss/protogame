(ns bronzeage.hexmap
  (:require [clojure.set :refer [difference]]))

(def rowlength 11)

(def xscale 74)
(def yscale 90)
(def adjust (/ yscale 2))

(def hexmap [2 2 2 2 2 1 2 2 1 1 2
             2 1 1 0 0 1 2 1 1 1 1
             1 0 1 0 0 0 1 0 0 1 1
             0 0 0 0 0 1 1 0 1 0 1
             0 0 0 0 0 0 0 0 0 1 1])

(def imgs ["images/hex-green.png" "images/hex-sand.png" "images/hex-mnt.png" "images/hexdot.png"])


(defn coord->array [x y]
  (+ x (* rowlength y)))

(defn array->coord [i]
  [(mod i rowlength) (int (/ i rowlength))])

(defn neighbor-hex-coords [x y]
  (let [adjust (mod x 2)
        coords #{[x (dec y)] [x (inc y)] [(dec x) y] [(inc x) y]}]
    (if (= 0 adjust)
      (into coords [[(dec x) (dec y)] [(inc x) (dec y)]])
      (into coords [[(dec x) (inc y)] [(inc x) (inc y)]]))))

(defn remove-oob-coords [x y]
  (let [coords (neighbor-hex-coords x y)]
    (cond-> coords
      (= 0 x) (difference #{[(dec x) y] [(dec x) (dec y)] [(dec x) (inc y)]})
      (= (- rowlength 1) x) (difference #{[(inc x) y] [(inc x) (dec y)] [(inc x) (inc y)]}))))

(defn neighbor-hex-arr [a b]
  (map (fn [[x y]] (coord->array x y)) (remove-oob-coords a b)))

(defn remove-oob-arr [a b]
  (let [myarr (neighbor-hex-arr a b)]
    (->> myarr
         (filter #(< -1 %))
         (filter #(< % (count hexmap))))))

(defn hexagon [i img]
  (let [[x y] (array->coord i)
        adj (mod x 2)
        top (+ 100 (* y yscale) (* adjust adj))
        left (+ 20 (* x xscale))]
    [:div {:style {:position "absolute"
                   :top top :left left}}
     [:img {:src img
            :alt (str x ":" y)
            :style {:width "100px" :height "90px"}}]]))

(defn hexagon-mapper []
  [:div
   (for [i (range (count hexmap))]
     ^{:key i} [hexagon i (get imgs (get hexmap i))])
   (for [i (remove-oob-arr 9 4)]
     ^{:key (str "dot" i)} [hexagon i (get imgs 3)])])