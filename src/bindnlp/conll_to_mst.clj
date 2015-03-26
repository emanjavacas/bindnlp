(ns bindnlp.conll_to_mst)

(def root-dir "/Users/quique/corpora/")
(def in-fn "tiger_release_aug07.corrected.16012013.conll09")

(defn lazy-sent
  "lazy seq over conll 'block' sents"
  [file]
  (letfn [(aux [rdr]
            (lazy-seq
             (if-let [line (.readLine rdr)]
               (cons (acc rdr [line]) (aux rdr))
               (do (.close rdr) nil))))
          (acc [rdr ls]
            (if-let [line (.readLine rdr)]
              (if (= "" line) ls
                  (acc rdr (conj ls line)))
              (do (.close rdr) nil)))]
    (aux (clojure.java.io/reader file))))

(defn filter-sent [s]
  (map (fn [w] ((juxt #(nth % 1) #(nth % 4) #(nth % 10) #(nth % 8)) w))
       (map #(clojure.string/split % #"\t") s)))

(defn dummify [filtered]
  (map (fn [[tkn pos l head]]
         [tkn pos "LAB" "0"])
       filtered))

(defn sent->str 
  "transform a seq of vec-tokens into MST-format"
  [s]
  (apply str 
         (flatten (interpose "\n" 
                             (map (partial interpose "\t")
                                  (apply map list s))))))

(defn convert
  "does the job, if the flag dummify is set to true
  it reencodes parsing information to dummy variables
  this is needed when testing on the gold standard"
  [out-fn dummify?]
  (with-open [wrt (clojure.java.io/writer (str root-dir out-fn))]
    (doseq [s (lazy-sent (str root-dir in-fn))
            :let [filtered (if dummify?
                             (dummify (filter-sent s))
                             (filter-sent s))]]
      (.write wrt (str (sent->str filtered) "\n\n")))))

;; (def s (first (lazy-sent path-to-corpus)))
;; (filter-sent s)
;; ([Ross NE PNC 3] [Perot NE SB 4] [wäre VAFIN -- 0] [vielleicht ADV MO 4] [ein ART NK 8] [prächtiger ADJA NK 8] [Diktator NN PD 4])
;; (dummify (filter-sent s))
