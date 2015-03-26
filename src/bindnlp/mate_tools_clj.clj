(ns bindnlp.mate-tools-clj
  (:import  [is2.data SentenceData09]
            [is2.lemmatizer Lemmatizer]
            [is2.tag Tagger]
            [is2.mtag.Tagger]
;            [is2.parser Parser]
            ))

;; "https://code.google.com/p/mate-tools/"

(defn create-lemmatizer [model-fn]
  (let [lemmatizer (Lemmatizer. model-fn)
        sent-holder (SentenceData09.)]
    (fn [tokens]
      (do (.init sent-holder (into-array String (cons "<root>" tokens)))
          (.apply lemmatizer sent-holder))
      (map (fn [l t] {:lemma l :token t})
           (rest (for [i (.plemmas sent-holder)] i))
           tokens))))

(defn create-tagger [model-fn]
  (let [tagger (Tagger. model-fn)
        sent-holder (SentenceData09.)]
    (fn [tokens]
      (do (.init sent-holder (into-array String (cons "<root>" tokens)))
          (.apply tagger sent-holder))
      (map (fn [p t] {:pos p :token t})
           (rest (for [i (.ppos sent-holder)] i))
           tokens))))

;; (defn create-mtagger [model-fn]
;;   (let [mtagger (is2.mtag.Tagger model-fn)
;;         sent-holder (SentenceData09.)]
;;     (fn [tokens]
;;       (do (.init sent-holder (into-array String tokens))
;;           (.apply tagger sent-holder)
;;           (.apply mtagger sent-holder))
;;       (for [i (.pfeats sent-holder)] i))))

;; (let [sent-holder (SentenceData09.)]
;;   (defn mate-parse-de
;;     [tokens]
;;     (let [tokens (cons "<root>" tokens)]
;;       (.init sent-holder (into-array String tokens))
;;       (.apply mate-tagger-de sent-holder)
;;       (let [parsed (.apply mate-parser-de sent-holder)]
;;         (for [[form head head-idx label]
;;               (map vector 
;;                    (.forms parsed) 
;;                    (map #(nth tokens %) (.pheads parsed))
;;                    (.pheads parsed) 
;;                    (.plabels parsed))]
;;           {:token form :head head :head-idx head-idx :label label})))))

;; (mate-lemmatize-de ["Amazon.de" "ist" "ebenfalls" "zuständig" "für" "die" "Lieferung" "sowie" "alle" "Gewährleistungsansprüche" "." ])
;; (mate-lemmatize-de ["Die" "Preise" "verstehen" "sich" "gegebenenfalls" "zzgl." "Versandkosten"])

;; (mate-tag-de ["Amazon.de" "ist" "ebenfalls" "zuständig" "für" "die" "Lieferung" "sowie" "alle" "Gewährleistungsansprüche" "." ])
;; (mate-tag-de ["Die" "Preise" "verstehen" "sich" "gegebenenfalls" "zzgl." "Versandkosten"])

;; (mate-mtag-de ["Amazon.de" "ist" "ebenfalls" "zuständig" "für" "die" "Lieferung" "sowie" "alle" "Gewährleistungsansprüche" "." ])
;; (mate-mtag-de ["Die" "Preise" "verstehen" "sich" "gegebenenfalls" "zzgl." "Versandkosten"])

;; (mate-parse-de ["Amazon.de" "ist" "zuständig" "für" "die" "Lieferung" "und" "für" "alle" "Gewährleistungsansprüche" "." ])
;; (mate-parse-de ["Johannes" "trifft" "den" "Ball"])


