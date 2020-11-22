(ns hackvm-translator.parser
    (:require [clojure.string :as s]))

(def arith-commands #{"add" "sub" "neg" "eq" "gt" "lt" "and" "or" "not"})

(defn split-file
  [filename]
  (s/split (slurp filename) #"\r\n"))


(defn skip-line?
  [line]
  (or (= line "") (s/starts-with? line "//")))

(defn trim-file
  [filename]
  (let [lines (split-file filename)]
    (map #(s/split % #" ") (remove skip-line? lines))))

(defn arithmetic-command?
  [command]
  (and 
   (= 1 (count command))
   (contains? arith-commands (first command))))

(defn push-command?
  [command]
  (and 
   (= 3 (count command))
   (= (first command) "push")))

(defn pop-command?
  [command]
  (and 
   (= 3 (count command))
   (= (first command) "pop")))

(defn command-type
  [command]
  (cond
    (arithmetic-command? command) "ARITHMETIC"
    (push-command? command) "PUSH"
    (pop-command? command) "POP"
    :else "unknown"))

(defn get-commands-from-file
  [filename]
  (map #(cons (command-type %) %) (trim-file filename)))


