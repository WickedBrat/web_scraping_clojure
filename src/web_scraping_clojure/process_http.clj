(ns web-scraping-clojure.process-http
  (:require [net.cgrand.enlive-html :as html]
            [org.httpkit.client :as http]
            [clojure.string :as strings])
  (:gen-class))


(defn get-html-dom
  [url]
  (println "sending req to: " url)
  (html/html-snippet
   (:body @(http/get (str "http://books.toscrape.com/" url) {:insecure? true}))))


(defn extract-titles
  [dom]
  (apply hash-map (reduce into (map
   (fn [key] [(strings/lower-case (strings/trim (apply str (get key :content)))), ((key :attrs) :href)])
   (html/select dom [:ul.nav :li :ul :li :a])))))

(defn parse-page-for-books-url
  [page-dom]
  (map
    (fn [key] ((key :attrs) :href))
    (html/select page-dom [:article.product_pod :h3 :a]))
)

(defn parse-book-details
  [book-page-dom]
  (let [title (map
               (comp first :content)
               (html/select book-page-dom [:div.product_main :h1]))]
    (let [description (map
                 (comp first :content)
                 (html/select book-page-dom [:article.product_page :p]))]
      {:title title, :description description}))
)

(defn url-without-page
  [url]
  (strings/join "/" (drop-last (strings/split url #"/"))))

(defn get-url
  [url page-number]
  (str (url-without-page url) page-number)
)

(defn fetch-books
  [url pages]
  (println url pages)
  (let [books-urls (pmap (fn [page-number]
                           (parse-page-for-books-url (get-html-dom (get-url url page-number)))) pages)]
    
    (pmap (fn [book-url]
            (parse-book-details (get-html-dom (str (url-without-page url) "/" book-url)))) (first books-urls)))
)

(defn initialize-list
  []
  (let [category-url-map (extract-titles (get-html-dom ""))] category-url-map))
