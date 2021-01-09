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



(defn x-command?
  [command command-name len cb]
  (and
   (= len (count command))
   (cb command command-name)))

(defn command-name-eq? [command command-name] (= (first command) command-name))
(defn arithmetic-command? [command _] (contains? arith-commands (first command)))

(defn command-type
  [command]
  (let [get-command (partial x-command? command)]
  (cond
    (get-command  "" 1 arithmetic-command?) "ARITHMETIC"
    (get-command "push" 3 command-name-eq?) "PUSH"
    (get-command "pop" 3 command-name-eq?) "POP"
    (get-command "label" 3 command-name-eq?) "LABEL"
    :else "unknown")
   )
)

(defn get-commands-from-file
  [filename]
  (map #(cons (command-type %) %) (trim-file filename)))


