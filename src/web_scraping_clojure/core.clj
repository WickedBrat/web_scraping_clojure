(ns web-scraping-clojure.core
  (:require [web-scraping-clojure.process-http :as http-service])
  (:gen-class))

(def category-list (http-service/initialize-list))
; (def a :1-2)
(defn list-categories
  []
  (println (map (fn [category] category) (keys category-list)))
)

(defn parse-page-numbers
  [list-of-page-numbers]
  
  (map (fn [page-number]
       
        (if (not (keyword? page-number))
         (if (= page-number 1)
          "/index.html"
         (str "/page-" page-number ".html"))
         (if (not (= page-number :all))
          (parse-page-numbers (range (Integer. (get (clojure.string/split (get (clojure.string/split (str page-number) #"-") 0) #":") 1))  (+ (Integer.(get (clojure.string/split (str page-number) #"-") 1)) 1)))
          )
         )
         ) list-of-page-numbers)
)

(defn scrape
  [category-args]

  (println category-args)
  (map (fn [category-arg]
         (let [url (get category-list (name (key category-arg)))]
           (http-service/fetch-books url (flatten (parse-page-numbers (val category-arg)) )  ))) category-args))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Please enter through repl to run all the commands.")
)


