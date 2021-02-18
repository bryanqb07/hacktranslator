(ns hackvm-translator.parser
    (:require [clojure.string :as s]))

(def arith-commands #{"add" "sub" "neg" "eq" "gt" "lt" "and" "or" "not"})

(defn split-file
  [filename]
  (s/split (slurp filename) #"\r\n"))


(defn skip-line?
  [line]
  (or (= line "") (s/starts-with? line "//")))


(defn strip-line
  [line]
  (let [split-line (s/split line #" ") skip-rest (atom 0)]
    (reduce
     (fn [acc, cv]
       (if (or (= cv "//") (= cv ""))
         (swap! skip-rest inc))
         (if (> @skip-rest 0)
           acc
           (conj acc (s/trim cv)))
       )
     []
     split-line
     )
))

(defn trim-file
  [filename]
  (let [lines (split-file filename)]
    (map strip-line (remove skip-line? lines))))

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
    (get-command "label" 2 command-name-eq?) "LABEL"
    (get-command "goto" 2 command-name-eq?) "GOTO"
    (get-command "if-goto" 2 command-name-eq?) "IF-GOTO"
    (get-command "function" 3 command-name-eq?) "FUNCTION"
    (get-command "return" 1 command-name-eq?) "RETURN"
    :else "unknown")
   )
)

(defn get-commands-from-file
  [filename]
  (map #(cons (command-type %) %) (trim-file filename)))


