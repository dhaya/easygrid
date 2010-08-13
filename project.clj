(defproject easygrid "1.0.0-SNAPSHOT"
  :description "A template project using clojure and gridgain"
  :dependencies [[org.clojure/clojure "1.2.0-beta1"]
                 [org.clojure/clojure-contrib "1.2.0-beta1"]
		 [org.gridgain/gridgain "2.1.1"]]
  :dev-dependencies [[lein-javac "1.2.1-SNAPSHOT"]
		     [swank-clojure "1.2.1"]
		     [robert/hooke "1.0.2"]
		     [lein-run "1.0.0-SNAPSHOT"]]
  :aot [easygrid.GridTask]
  :source-path "src/clj"
  :java-source-path "src/java"
  :repositories {"gridgain" "http://www.gridgainsystems.com/maven2"})
