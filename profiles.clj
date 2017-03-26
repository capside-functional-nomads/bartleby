{:dev
 {:cljsbuild {:builds [{
                        :source-paths ["src/cljs"]
                        :compiler {:main bartleby.core
                                   :output-to "resources/public/js/compiled/app.js"
                                   :output-dir "resources/public/js/compiled/out"
                                   :asset-path "js/compiled/out"
                                   :source-map-timestamp true
                                   :preloads [devtools.preload]
                                   :external-config {:devtools/config {:features-to-install :all}}}}]}}

 :prod
 {:cljsbuild {:builds [{
                        :source-paths ["src/cljs"]
                        :compiler {:main bartleby.core
                                   :output-to "resources/public/js/compiled/app.js"
                                   :optimizations :advanced
                                   :pretty-print false
                                   :source-map-timestamp true}}]}}
 }
