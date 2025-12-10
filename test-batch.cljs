;; Batch test for newly exposed symbols
;; Run with: node cli.js test-batch.cljs

(println "=== Testing Batch Exposed Symbols ===")
(println)

;; Types to test with instance? (20 types added to nbb)
(def type-tests
  [['List '(list 1 2 3)]
   ['Cons '(cons 1 [2 3])]
   ['IndexedSeq '(seq [1 2 3])]
   ['ChunkedSeq '(seq (vec (range 100)))]
   ['ChunkedCons '(chunk-cons (chunk-first (seq (vec (range 100)))) nil)]
   ['Subvec '(subvec [1 2 3 4] 1 3)]
   ['PersistentHashMap '(hash-map :a 1 :b 2 :c 3 :d 4 :e 5 :f 6 :g 7 :h 8 :i 9)]
   ['PersistentHashSet '(hash-set 1 2 3 4 5 6 7 8 9)]
   ['PersistentArrayMap '{:a 1 :b 2}]
   ['PersistentTreeMap '(sorted-map :a 1 :b 2)]
   ['PersistentTreeSet '(sorted-set 1 2 3)]
   ['Repeat '(repeat 5 :x)]
   ['Cycle '(cycle [1 2])]
   ['Iterate '(iterate inc 0)]
   ['IntegerRange '(range 10)]
   ['Range '(range 0.5 10 0.5)]
   ['Volatile '(volatile! 42)]
   ['TaggedLiteral '(tagged-literal 'foo {:bar 1})]
   ['ExceptionInfo '(ex-info "test" {:code 42})]
   ['Reduced '(reduced 42)]])

(println "--- Types ---")
(doseq [[type-sym expr] type-tests]
  (let [full-sym (symbol "cljs.core" (str type-sym))]
    (try
      (if-let [t (resolve full-sym)]
        (let [type-val (if (var? t) @t t)
              test-val (eval expr)]
          (if (instance? type-val test-val)
            (println "  OK:" type-sym)
            (println "FAIL:" type-sym "- instance? returned false")))
        (println "MISS:" type-sym "- not resolvable"))
      (catch :default e
        (println " ERR:" type-sym "-" (.-message e))))))

(println)
(println "--- Functions (9 added to nbb) ---")

;; Test predicates
(try (println "  OK: infinite? -" (infinite? js/Infinity) (infinite? 42))
  (catch :default e (println " ERR: infinite? -" (.-message e))))
(try (println "  OK: regexp? -" (regexp? #"test") (regexp? "test"))
  (catch :default e (println " ERR: regexp? -" (.-message e))))
(try (println "  OK: iterable? -" (iterable? [1 2]))
  (catch :default e (println " ERR: iterable? -" (.-message e))))
(try (println "  OK: cloneable? -" (cloneable? [1 2]))
  (catch :default e (println " ERR: cloneable? -" (.-message e))))
(try (println "  OK: reduceable? -" (reduceable? [1 2]))
  (catch :default e (println " ERR: reduceable? -" (.-message e))))
(try (println "  OK: js-symbol? -" (js-symbol? (js/Symbol "x")) (js-symbol? :x))
  (catch :default e (println " ERR: js-symbol? -" (.-message e))))
(try (println "  OK: Throwable->map -" (some? (Throwable->map (ex-info "test" {}))))
  (catch :default e (println " ERR: Throwable->map -" (.-message e))))
(try (println "  OK: js-invoke -" (js-invoke #js {:foo (fn [x] (+ x 10))} "foo" 5))
  (catch :default e (println " ERR: js-invoke -" (.-message e))))
(try (println "  OK: js-mod -" (js-mod 10 3))
  (catch :default e (println " ERR: js-mod -" (.-message e))))

(println)
(println "--- Protocols (satisfies? tests) ---")

(def protocol-tests
  [['ISeqable [1 2 3]]
   ['ISequential [1 2 3]]
   ['ICollection [1 2 3]]
   ['ICounted [1 2 3]]
   ['IEmptyableCollection [1 2 3]]
   ['IAssociative {:a 1}]
   ['IMap {:a 1}]
   ['IMapEntry (first {:a 1})]
   ['ISet #{1 2 3}]
   ['IStack [1 2 3]]
   ['IVector [1 2 3]]
   ['IHash [1 2 3]]
   ['IEquiv [1 2 3]]
   ['IComparable 42]
   ['IReduce [1 2 3]]
   ['IKVReduce {:a 1}]
   ['IPending (delay 42)]
   ['IVolatile (volatile! 42)]
   ['IWatchable (atom 42)]
   ['ICloneable [1 2 3]]])

(doseq [[proto-sym test-val] protocol-tests]
  (let [full-sym (symbol "cljs.core" (str proto-sym))]
    (try
      (if-let [p (resolve full-sym)]
        (let [proto-val (if (var? p) @p p)]
          (if (satisfies? proto-val test-val)
            (println "  OK:" proto-sym)
            (println "FAIL:" proto-sym "- satisfies? returned false")))
        (println "MISS:" proto-sym "- not resolvable"))
      (catch :default e
        (println " ERR:" proto-sym "-" (.-message e))))))

(println)
(println "=== Done ===")
