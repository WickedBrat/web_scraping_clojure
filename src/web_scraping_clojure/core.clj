(ns web-scraping-clojure.core
  (:require [web-scraping-clojure.process-http :as http-service])
  (:gen-class))

(def category-list (http-service/initialize-list))

(defn list-categories
  []
  (println (map (fn [category] category) (keys category-list)))
)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Please enter through repl to run all the commands.")
)
