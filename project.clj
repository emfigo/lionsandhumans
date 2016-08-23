(defproject lionsandhumans "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :min-lein-version "2.5.1"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[com.novemberain/langohr "3.6.1"]
                 [com.taoensso/carmine "2.14.0"] ;; Stable
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/data.json "0.2.6"]]
  :plugins [[lein-environ "1.0.1"]
            [s3-wagon-private "1.1.2"]
            [lein-gen "0.2.2"]])


