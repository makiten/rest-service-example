(ns rest-service.routes.courses
    (:require [compojure.api.sweet :refer :all]
              [rest-service.util :refer [load-config]]
              [clojure.java.jdbc :as sql]
              [ring.util.http-response :as respond]))

(def h2-spec-db (load-config "./resources/db/config.edn"))
(def h2-spec (h2-spec-db :database))

(defn get-course
    "Gets the courses for our coding academy."
    ([]
        (let [courses (sql/query h2-spec
                                 ["SELECT * FROM courses"])]
            (respond/ok courses)))
    ([id]
        (let [course (sql/query h2-spec
                                ["SELECT * FROM courses WHERE id = ?" id])]
            (if (empty? course)
                (respond/bad-request {:error "Bad request"})
                (respond/ok course)))))

(defn add-course
    "Adds a new course."
    [title description]
    (let [result (sql/insert! h2-spec :courses {:title title :description description})]
        (if (empty? result)
            (respond/bad-request {:error "Bad request"})
            (respond/created {} result))))

(defn update-course
    "Updates existing course"
    [id title description]
    (let [result (sql/update! h2-spec :courses {:title title :description description} ["id = ?" id])]
        (if (empty? result)
            (respond/bad-request {:error "Bad request"})
            (respond/ok result))))

(defn delete-course
    "Deletes an existing course"
    [id]
    (let [result (sql/delete! h2-spec :courses ["id = ?" id])]
        (if (empty? result)
            (respond/bad-request {:error "Bad request"})
            (respond/ok result))))


(def courses-routes
    (context "/api/course" []
        :tags ["course"]
        (GET    "/"  {:as request}
            :summary "Gets all the courses available."
            (get-course))
        (POST   "/"      {:as request}
            :return      {:id Long}
            :summary     "Adds a new course."
            :body-params [title :- String description :- String]
            (add-course title description))
        (GET    "/:id"   {:as request}
            :return      {:title String :description String}
            :summary     "Gets an individual course data."
            :path-params [id :- Long]
            (get-course id))
        (PATCH  "/:id"   {:as request}
            :return      {:updated? Boolean}
            :summary     "Updates the course."
            :path-params [id :- Long]
            :body-params [{title :- String ""} {description :- String ""}]
            (update-course id title description))
        (DELETE "/:id"   {:as request}
            :return      {:deleted? Boolean}
            :summary     "Deletes a course."
            :path-params [id :- Long]
            (delete-course id))))