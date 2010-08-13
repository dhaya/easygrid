(ns easygrid.GridTask
  (:gen-class :extends org.gridgain.grid.GridTaskSplitAdapter
	      :load-impl-ns false))

(defn job
  [shard]
  (proxy [org.gridgain.grid.GridJobAdapter] [(into-array shard)]
    (execute []
	     (let [sum (apply + shard)]
	       (println "Shard: " shard)
	       (println "Count: " (count shard))
	       (println "Sum: " sum)
	       sum))))
(defn -split
  [this gridsize integers]
  (->> integers
       (split-at (/ (count integers) 2))       
       (map job)))

(defn -reduce
  [this results]
  (->> results
       (map #(.getData %))
       (apply +)))
