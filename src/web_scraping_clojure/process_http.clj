(ns web-scraping-clojure.process-http
  (:require [net.cgrand.enlive-html :as html]
            [org.httpkit.client :as http]
            [clojure.string :as strings])
  (:gen-class))


(defn get-html-dom
  [url]
  (html/html-snippet
   (:body @(http/get (str "http://books.toscrape.com/" url) {:insecure? true}))))

(defn extract-titles
  [dom]
  (apply hash-map (reduce into (map
   (fn [key] [(strings/trim (apply str (get key :content))), ((key :attrs) :href)]) (html/select dom [:ul.nav :li :ul :li :a])))))

(defn initialize-list
  []
  (let [category-url-map (extract-titles (get-html-dom ""))] category-url-map))
