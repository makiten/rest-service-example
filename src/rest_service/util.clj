(ns rest-service.util
    (:require [clojure.edn :as edn]))

(defn load-config
    [filename]
    (edn/read-string (slurp filename)))