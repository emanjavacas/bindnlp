(ns bindnlp.tt4j-clj
    (:import [org.annolab.tt4j TreeTaggerWrapper TokenHandler ProbabilityHandler]))

;;; Inspired in https://github.com/linkfluence/clj-treetagger/blob/master/src/clj_treetagger.clj
(def ^{:private true} home "/usr/local/share/treetagger/")
(def ^{:private true} models "/usr/local/share/treetagger/lib/")

(defn init-tt4j [home model]
  (System/setProperty "treetagger.home" home)
  (let [tagger (TreeTaggerWrapper.)]
    (doto tagger
      (.setModel model))))

(defn- tag-tokens [tagger tokens]
  (let [collect (atom [])
        probs (atom [])]
    (doto tagger
      (.setProbabilityThreshold 0.85)
      (.setHandler
       (reify
         TokenHandler
         (token [this token pos lemma]
           (swap! collect (fn [arr] (conj arr {:token token :pos pos :lemma lemma}))))
         ProbabilityHandler
         (probability [this pos lemma probability]
           (swap! probs (fn [arr] (conj arr probability)))))))
    (.process tagger tokens)
    (map (fn [tok prob] (assoc tok :prob prob)) @collect @probs)))

(defn create-tt4j [lang]
  (let [model (case lang 
                :nl "dutch-utf8.par"
                :de "german-utf8.par"
                :es "spanish-utf8.par"
                :en "english.par")
        tagger (init-tt4j home (str models model))]
    (fn [tokens] (tag-tokens tagger tokens))))

;; (def tt4j-de (create-tt4j :de))
;; (tt4j-de ["Ich" "hoffe" "," "dass" "dieses" "Ding" "gut" "funktionieren" "kann"])

;; (time (def tagger (init-tt4j home (str models "german-utf8.par")))) >>> "Elapsed time: 0.246 msecs"
;; (def sent ["Ich" "hoffe" "," "dass" "dieses" "Ding" "gut" "funktionieren" "kann"])
;; (time (tag-tokens tagger sent)) >>> "Elapsed time: 4257.892 msecs"

