(ns dev
  (:refer-clojure :exclude [test])
  (:require [clojure.repl :refer :all]
            [clojure.pprint :refer [pprint]]
            [clojure.tools.namespace.repl :refer [refresh set-refresh-dirs]]
            [clojure.java.io :as io]
            [com.stuartsierra.component :as component]
            [duct.generate :as gen]
            [duct.util.repl :refer [setup test cljs-repl migrate rollback]]
            [duct.util.system :refer [load-system]]
            [reloaded.repl :refer [system init start stop go reset]]))

(defn new-system []
  (load-system (keep io/resource ["functional/system.edn" "dev.edn" "local.edn"])))

(when (io/resource "local.clj")
  (load "local"))

(gen/set-ns-prefix 'functional)

(defn set-refresh-src!
  "Just set source as the refresh dirs"
  []
  (set-refresh-dirs "./src" "./dev"))

(set-refresh-dirs "./src" "./dev")

(reloaded.repl/set-init! new-system)
