(defproject bartleby "0.1.0-SNAPSHOT"
  :description "CAPSiDE Functional Nomads: Session 03"
  :url "https://github.com/capside-functional-nomads/bartleby"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring/ring-core "1.5.1"]
                 [ring/ring-devel "1.5.1"]
                 [ring-logger "0.7.7"]
                 [ring-logger-timbre "0.7.5"]
                 [metosin/ring-http-response "0.8.2"]
                 [ring/ring-json "0.4.0"]
                 [com.taoensso/timbre "4.8.0"]
                 [com.fzakaria/slf4j-timbre "0.3.4"]
                 [cprop "0.1.10"]
                 [org.immutant/web "2.1.6"
                  :exclusions [ch.qos.logback/logback-classic]]
                 [compojure "1.5.2"]
                 [com.stuartsierra/component "0.3.2"]
                 [hikari-cp "1.7.5"]
                 [com.layerware/hugsql "0.4.7"]
                 [migratus "0.8.33"]
                 [org.postgresql/postgresql "42.0.0"]
                 [buddy/buddy-core "1.2.0"]
                 [buddy/buddy-sign "1.4.0"]
                 [buddy/buddy-auth "1.4.1"]
                 [clj-time "0.13.0"]
                 [clj-http "2.3.0"]
                 [cheshire "5.7.0"]
                 [reloaded.repl "0.2.3"]
                 [org.clojure/tools.namespace "0.2.11"]]
  :main bartleby.core
  ;;:main ^:skip-aot bartleby.core
  :target-path "target/%s"
  :profiles {:dev {:resource-paths ["config/dev"]}
             :prod {:resource-paths ["config/prod"]}
             :uberjar {:aot :all}})
