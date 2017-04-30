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
                (respond/ok (first course))))))

(defn add-course
    "Adds a new course."
    [title description]
    (let [result (sql/insert! h2-spec :courses {:title title :description description})]
        (if (empty? result)
            (respond/bad-request {:error "Bad request"})
            (respond/created {} (:scope_identity() (first result))))))

(defn update-course
    "Updates existing course"
    [id title description]
    (let [result (sql/update! h2-spec :courses {:title title :description description} ["id = ?" id])]
        (if (empty? result)
            (respond/bad-request {:error "Bad request"})
            (respond/ok {:updated? (= 1 (first result))}))))

(defn delete-course
    "Deletes an existing course"
    [id]
    (let [result (sql/delete! h2-spec :courses ["id = ?" id])]
        (if (empty? result)
            (respond/bad-request {:error "Bad request"})
            (respond/ok {:deleted? (= 1 (first result))}))))


(def courses-routes
    (context "/api/course" []
        :tags ["course"]
        (GET    "/"  {:as request}
            :summary "Gets all the courses available."
            :description "See all the courses offered."
            (get-course))
        (POST   "/"      {:as request}
            :return      {:id Long}
            :summary     "Adds a new course."
            :description "Add a new course offering."
            :body-params [title :- String description :- String]
            (add-course title description))
        (GET    "/:id"   {:as request}
            :return      {:id Long :title String :description String}
            :summary     "Gets an individual course data."
            :description "Find an individual course and get its data."
            :path-params [id :- Long]
            (get-course id))
        (PATCH  "/:id"   {:as request}
            :return      {:updated? Boolean}
            :summary     "Updates the course."
            :description "Find and update a course with a new title and description."
            :path-params [id :- Long]
            :body-params [{title :- String ""} {description :- String ""}]
            (update-course id title description))
        (DELETE "/:id"   {:as request}
            :return      {:deleted? Boolean}
            :summary     "Deletes a course."
            :description "Deletes a course from the offerings."
            :path-params [id :- Long]
            (delete-course id))))