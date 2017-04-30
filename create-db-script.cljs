(require '[clojure.java.jdbc :as sql])

(def h2-spec {:classname "org.h2.Driver"
              :subprotocol "h2:file"
              :subname "./resources/db/rest-service"
              :user ""
              :password "" })

(sql/with-db-connection [db-conn h2-spec]
    (sql/db-do-commands h2-spec
        (sql/create-table-ddl :courses
            [[:id "bigint primary key auto_increment"]
             [:title "varchar(255)"]
             [:description "varchar(4096)"]]))
    (sql/insert-multi! db-conn :courses
        [{:title "Intro to Clojure" :description "Clojure is a fun Lisp dialect compatible with multiple software environments."}
         {:title "Intro to Python" :description "Python is a popular general purpose language that has grown in use in data science."}
         {:title "Software Project Management" :description "Missed requirements are a disease. Meet the cure."}
         {:title "UI Development with Vue.js" :description "Build great UIs with a MVVM tool that gets out of your way to let you get things done."}
         {:title "Getting Started with Docker" :description "Learn how to compartmentalize and containerize applications in Docker."}
         {:title "Setting up a LAMP Server" :description "An old common and reliable stack learn how to build a tech stack that'll take you places."}]))