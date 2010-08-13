(ns easygrid.launcher
  (:use easygrid.GridTask)
  (:import (org.gridgain.grid GridFactory)))

(defmacro with-grid
  [g & body]
  (println body)
  `(try
     (GridFactory/start)
     (let [~g (GridFactory/getGrid)]
       ~@body)
     (finally
      (GridFactory/stop true))))

(defn start
  []
  (with-grid grid
    (let [grid-future (.execute grid easygrid.GridTask (range 20))]
      (println "Final Sum: " (.get grid-future)))))
