(ns rest-service.handler
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [rest-service.routes.courses :refer [courses-routes]]))

(def app
  (api
    {:swagger
     {:ui "/"
      :spec "/swagger.json"
      :data {:info {:title "Intro to Clojure REST Service example"
                    :description "A (very) simple REST service made in Clojure."}
             :tags [{:name "api", :description "some apis"}]}}}
    courses-routes))