(ns bindnlp.berkeley-clj
  (:import [edu.berkeley.nlp.PCFGLA ParserData CoarseToFineMaxRuleParser TreeAnnotations]
           [edu.berkeley.nlp.syntax Tree]
           [edu.berkeley.nlp.util Numberer]))

(defn- vectorize
  "transforms an output tree into vector form"
  [nested-seqs]
  (if (seq? nested-seqs)
    (mapv vectorize (filter #(not (and (seq? %) (empty? %))) nested-seqs))
    nested-seqs))

(defn create-berkeley-parser
  "returns a parsing function for a given language
  you need to have the models in a folder models/ 
  living in the top project folder"
  [lang]
  (let [model (str "models/" (case lang
                               :de "ger_sm5.gr"
                               :en "eng_sm6.gr"
                               :fr "fra_sm5.gr"))
        pdata (-> (clojure.java.io/as-file model)
                  .getAbsolutePath
                  ParserData/Load)
        ;; some hints on setup from mochii and berkeley sources
        config-parser (do (-> pdata
                              .getNumbs
                              Numberer/setNumberers)
                          (CoarseToFineMaxRuleParser.
                           (.getGrammar pdata)
                           (.getLexicon pdata)
                           1.0 -1 false false false false false true true))]
    (fn [tok-sent]
      (-> config-parser
          (.getBestParse tok-sent)
          (TreeAnnotations/unAnnotateTree false)
          ;      .toString
          ;      read-string
          ;     vectorize
          ))))


;;; ----------------
;;; Berkeley Parser
;;; ----------------

;; (.toString (parse-sentence (nth (map tokenize-de (apply get-sents-de (:content (second (:content (first trees-de)))))) 7)))

;; (nth (map tokenize-de (apply get-sents-de (:content (second (:content (first trees-de)))))) 7)

;; (doseq [s (map tokenize-de (apply get-sents-de (:content (second (:content (second trees-de))))))]
;;   (println (parse-sentence s)))
