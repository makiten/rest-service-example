(defproject rest-service "0.1.0"
  :description "REST service for Intro to Clojure"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [metosin/compojure-api "1.1.10"]
                 [cheshire "5.7.1"]
                 [com.h2database/h2 "1.4.195"]
                 [org.clojure/java.jdbc "0.7.0-alpha3"]]
  :ring {:handler rest-service.handler/app}
  :uberjar-name "server.jar"
  :profiles {:dev  {:dependencies [[javax.servlet/javax.servlet-api "3.1.0"]]
                    :plugins [[lein-ring "0.10.0"]]}})
