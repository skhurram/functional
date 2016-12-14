(defproject functional "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.8.40"]
                 [org.clojure/core.async "0.2.391"
                  :exclusions [org.clojure/tools.reader]]
                 [cljsjs/react "15.3.1-0"]
                 [cljsjs/react-dom "15.3.1-0"]
                 [sablono "0.7.3"]
                 [prismatic/om-tools "0.4.0" :exclusions [prismatic/schema]]
                 [org.omcljs/om "1.0.0-alpha46" :exclusions [cljsjs/react]]
                 ;[org.clojars.skhurram/cljs-react-material-ui "0.2.31-SNAPSHOT"]
                 [cljsjs/fixed-data-table "0.6.3-0"]

                 [secretary "1.2.2"]

                 [cljs-ajax "0.5.8" :exclusions [com.cognitect/transit-cljs]]
                 [com.cognitect/transit-cljs "0.8.239"]

                 [com.stuartsierra/component "0.3.1"]
                 [compojure "1.5.1"]
                 [metosin/compojure-api "1.1.9"]
                 [metosin/schema-tools "0.9.0"]
                 [duct "0.8.2"]
                 [environ "1.1.0"]
                 [adzerk/env "0.3.1"]
                 [com.andrewmcveigh/cljs-time "0.4.0"]

                 ;;[hanami "0.1.1"]
                 [org.clojars.skhurram/hanami "0.1.1-SNAPSHOT"]
                 [ring "1.5.0"]
                 [ring/ring-defaults "0.2.1"]
                 [ring-jetty-component "0.3.1"]
                 [ring-webjars "0.1.1"]
                 [org.slf4j/slf4j-nop "1.7.21"]
                 [org.webjars/normalize.css "3.0.2"]
                 [yesql "0.5.3"]
                 [duct/hikaricp-component "0.1.0"]
                 [mysql/mysql-connector-java "6.0.5"]
                 [duct/ragtime-component "0.1.4"]]
  :plugins [[lein-environ "1.0.3"]
            [lein-cljsbuild "1.1.2"]]
  :main ^:skip-aot functional.main
  :uberjar-name "functional-standalone.jar"
  :target-path "target/%s/"
  :resource-paths ["resources" "target/cljsbuild"]
  :prep-tasks [["javac"] ["cljsbuild" "once"] ["compile"]]
  :cljsbuild
  {:builds
   [{:id "main"
     :jar true
     :source-paths ["src"]
     :compiler
     {:output-to     "target/cljsbuild/functional/public/js/main.js"
      :optimizations :advanced}}]}
  :aliases {"setup"  ["run" "-m" "duct.util.repl/setup"]
            "deploy" ["do"
                      ["vcs" "assert-committed"]
                      ["vcs" "push" "heroku" "master"]]}
  :profiles
  {:dev  [:project/dev  :profiles/dev]
   :test [:project/test :profiles/test]
   :repl {:resource-paths ^:replace ["resources" "dev/resources" "target/figwheel"]
          :prep-tasks     ^:replace [["javac"] ["compile"]]}
   :uberjar {:aot :all}
   :profiles/dev  {}
   :profiles/test {}
   :project/dev   {:dependencies [[duct/generate "0.8.2"]
                                  [reloaded.repl "0.2.3"]
                                  [org.clojure/tools.namespace "0.2.11"]
                                  [org.clojure/tools.nrepl "0.2.12"]
                                  [eftest "0.1.1"]
                                  [com.gearswithingears/shrubbery "0.4.1"]
                                  [kerodon "0.8.0"]
                                  [binaryage/devtools "0.8.2"]
                                  [com.cemerick/piggieback "0.2.1"]
                                  [duct/figwheel-component "0.3.3"]
                                  [figwheel "0.5.8"]]
                   :source-paths   ["dev/src"]
                   :resource-paths ["dev/resources"]
                   :repl-options {:init-ns user
                                  :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
                   :env {:port "3000"}}
   :project/test  {}})
