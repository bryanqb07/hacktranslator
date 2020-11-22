(ns hackvm-translator.generator
    (:require [clojure.string :as s]))

(def memory-symbols
  { "constant" "SP"
    "local" "LCL"
    "argument" "ARG"
    "this" "THIS"
    "that" "THAT"
    "temp" "TEMP"
   })

(defn addr [value] (str "@" value))
(defn load-pointer [pointer-sym] (list (addr pointer-sym) "A=M"))

(defn join-strings
  [strings]
  (s/join "\n" strings))

(defn split-command
  [command]
  (s/split command #" "))


(defn change-pointer
  [pointer-sym, func]
  (list (addr pointer-sym) (str "M=M" func 1)))

(defn decrement-pointer [pointer-sym] (change-pointer pointer-sym "-"))
(defn increment-pointer [pointer-sym] (change-pointer pointer-sym "+"))


(def sp++ (increment-pointer "SP"))
(def sp-- (decrement-pointer "SP"))

(defn load-pointer-val 
  [pointer-sym expr]
  (concat (load-pointer pointer-sym) (list expr) )
)


(defn d-into-pointer [pointer-sym] (load-pointer-val pointer-sym "M=D"))
(defn pointer-into-d [pointer-sym] (load-pointer-val pointer-sym "D=M"))
(defn pointer-into-ad [pointer-sym] (load-pointer-val pointer-sym "D=A"))


(defn load-val  [value] (list (addr value) "D=A"))

(defn push-constant-val
  [value]
  (concat 
   (load-val value)
   (d-into-pointer "SP")
   sp++   
   )
)

(defn offset-d [offset] (list (addr offset) "D=D+A"))

(defn push-with-offset 
  [memory-sym, offset] 
  (concat
   (pointer-into-ad memory-sym)
   (offset-d offset)
   '("A=D" "D=M")
   (d-into-pointer "SP")
   sp++
))

(defn offsetable? [memory-sym] #{"LCL" "ARG" "THIS" "THAT" "TEMP"})

(defn push-nonconstant-val
  [memory-sym offset]
  (cond
    (offsetable? memory-sym) (push-with-offset memory-sym offset)
    :else "unknown"
))

(defn push-val
  [memory-sym value]
  (if (= "SP" memory-sym)
    (push-constant-val value)
    (push-nonconstant-val memory-sym value)
))

(defn d-into-m [expr] (list "A=M" (str "D=" expr) "M=D"))
(defn transform-m [func] (list "A=M" (str "M=" func "M")))
(defn label [value] (str "(" value ")"))

(def counter (atom -1))

(defn inc-counter [] (let [result (swap! counter inc)] @counter))

(defn jump-with-cond
  [jump-sym]
  (let [jump-label (str "SKIP" (inc-counter))]
      (list
       "A=M"
       "D=D-M"
       "M=-1"
       (addr jump-label)
       (str "D;" jump-sym)
       (addr "SP")
       "A=M"
       "M=0"
       (label jump-label)
       )))

(defn arithmetic-pop
  [func op]
  (concat
   sp--
   (pointer-into-d "SP")
   sp--
   (func op)
   sp++
   ))

(defn arithmetic-pop-one
  [op]
  (concat
   sp--
   (transform-m op)
   sp++))


(def arithmetic-calc (partial arithmetic-pop d-into-m))
(def arithmetic-cmp (partial arithmetic-pop jump-with-cond))

(defn a_add [] (arithmetic-calc "D+M"))
(defn a_sub [] (arithmetic-calc "M-D"))
(defn a_or [] (arithmetic-calc "D|M"))
(defn a_and [] (arithmetic-calc "D&M"))
(defn a_eq [] (arithmetic-cmp "JEQ"))
(defn a_gt [] (arithmetic-cmp "JLT"))
(defn a_lt [] (arithmetic-cmp "JGT"))
(defn a_neg [] (arithmetic-pop-one "-"))
(defn a_not [] (arithmetic-pop-one "!"))


(defn call 
  [fname & args]
  (when-let [fun (ns-resolve 'hackvm-translator.generator (symbol fname))]
    (apply fun args)
))

(defn handle-arith-command [command] (call (str "a_" command)))

(defn handle-push-command
  [[memory-id value]]
  (let [memory-sym (get memory-symbols memory-id)]
    (push-val memory-sym value)))


(defn inc-addr [times] (repeat (Integer/parseInt times) "A=A+1"))


(defn indirectly-addressed? [memory-sym] (contains? #{"SP" "LCL" "ARG" "THIS" "THAT"} memory-sym))

(def temp-addr "@5")

(defn handle-pointer-offset
 [memory-sym] 
 (cond 
   (indirectly-addressed? memory-sym) (load-pointer memory-sym)
   (= "TEMP" memory-sym) (list temp-addr)
))

(defn handle-pop-offset
  [memory-sym, offset]
  (concat
   (handle-pointer-offset memory-sym)
   (inc-addr offset)
   '("M=D")
))

(defn pop-val
  [memory-sym offset]
  (concat
   sp--
   (pointer-into-d "SP")
   (handle-pop-offset memory-sym offset)
   )
)

(defn handle-pop-command
  [[memory-id value]]
  (let [memory-sym (get memory-symbols memory-id)]
    (pop-val memory-sym value)))


(defn process-command
  [[command-type & command]]
  (case command-type
    "ARITHMETIC" (handle-arith-command (first command))
    "PUSH" (handle-push-command (rest command))
    "POP" (handle-pop-command (rest command))
    (list "this is an unknown command")
    ))


(defn add-comment [command] (str "// " (s/join " " command)))

(defn generate-code
  [commands]
  (join-strings (mapcat #(cons (add-comment %) (process-command %)) commands)))

(defn spit-to-file
  [filename, commands]
  (spit filename (generate-code commands)))
