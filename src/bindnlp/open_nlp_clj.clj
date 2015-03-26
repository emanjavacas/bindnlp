(ns bind.open-nlp-clj
  (:require [opennlp.nlp :as nlp]
            [opennlp.treebank :as tb])) 

(def ^{:private true} utf-regex-de #"(\s\p{Pi}|\p{Ps}|\p{Pf}\s|\p{Pe}\s|\"|'\s|\s')" " $1 ")
(def ^{:private true} utf-regex-nl #"(\s\p{Pi}|\p{Pf}\s|\"|'\s|\s')" " $1 ")

;;; -----------
;;; GERMAN
;;; -----------
;;; Sentence boundary detection
(def get-sents-de (nlp/make-sentence-detector "models/de-sent.bin"))

;;; Tokenizer
(let [tokenizer (nlp/make-tokenizer "models/de-token.bin")]

  (defn tokenize-de [s]
    (tokenizer
     (clojure.string/replace s utf-regex))))

;;; POS
(def pos-tagger-de (nlp/make-pos-tagger "models/de-pos-maxent.bin"))

;;; -----------
;;; NL
;;; -----------
;;; Sentence boundary detection
(def get-sents-nl (nlp/make-sentence-detector "models/nl-sent.bin"))

;;; Tokenizer
(let [tokenizer (nlp/make-tokenizer "models/nl-token.bin")]

  (defn tokenize-nl [s]
    (tokenizer
     (clojure.string/replace s utf-regex-nl))))

;;; POS
(def pos-tagger-nl (nlp/make-pos-tagger "models/nl-pos-maxent.bin"))

;;; -----------
;;; Detokenizer (language agnostic?)
;;; ----------- 
(def detokenizer (nlp/make-detokenizer "models/english-detokenizer.xml"))
