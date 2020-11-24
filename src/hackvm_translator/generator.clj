(ns hackvm-translator.generator
    (:require [clojure.string :as s]))

(def memory-symbols
  { "constant" "SP"
    "local" "LCL"
    "argument" "ARG"
    "this" "THIS"
    "that" "THAT"
    "temp" "TEMP"
    "pointer" "PTR"
    "static" "STATIC"
   })


(defn indirectly-addressed? [memory-sym] (contains? #{"SP" "LCL" "ARG" "THIS" "THAT"} memory-sym))

(def temp-addr "@5")


(defn handle-pointer-offset
 [memory-sym f & fail-vals] 
 (cond 
   (indirectly-addressed? memory-sym) (f memory-sym)
   (= "TEMP" memory-sym) (concat (list temp-addr) fail-vals)
   :else "unknown"
))


(defn addr [value] (str "@" value))
(defn load-pointer [pointer-sym] (list (addr pointer-sym) "A=M"))

(defn load-pointer-val 
  [pointer-sym exp]
  (concat (load-pointer pointer-sym) (list exp) )
)

(defn d-into-pointer [pointer-sym] (load-pointer-val pointer-sym "M=D"))
(defn pointer-into-d [pointer-sym] (load-pointer-val pointer-sym "D=M"))
(defn pointer-into-ad [pointer-sym] (load-pointer-val pointer-sym "D=A"))
(defn get-pointer-offset-pop [memory-sym] (handle-pointer-offset memory-sym load-pointer))
(defn get-pointer-offset-push [memory-sym] (handle-pointer-offset memory-sym pointer-into-ad "D=A"))

(defn join-strings [strings] (s/join "\n" strings))

(defn split-command [command] (s/split command #" "))


(defn change-pointer
  [pointer-sym, func]
  (list (addr pointer-sym) (str "M=M" func 1)))

(defn decrement-pointer [pointer-sym] (change-pointer pointer-sym "-"))
(defn increment-pointer [pointer-sym] (change-pointer pointer-sym "+"))


(def sp++ (increment-pointer "SP"))
(def sp-- (decrement-pointer "SP"))
(def D=*SP (pointer-into-d "SP"))
(def *SP=D (d-into-pointer "SP"))

(defn pop-sp [] (concat sp-- D=*SP))
(defn push-sp [] (concat *SP=D sp++))

(defn load-val  [value] (list (addr value) "D=A"))

(defn push-constant-val
  [value]
  (concat 
   (load-val value)
   (push-sp)
   )
)

(defn offset-d [offset] (list (addr offset) "D=D+A"))

(defn push-with-offset 
  [memory-sym, offset] 
  (concat
   (get-pointer-offset-push memory-sym)
   (offset-d offset)
   '("A=D" "D=M")
   (push-sp)
))

(defn get-file-title [] 
  (let [filename (eval 'hackvm-translator.core/filename)] 
    (first (s/split filename #".vm"))))

(defn generate-static-label
  [offset]
  (let [filename (get-file-title)]
      (str (addr filename) "." offset)
))


(defn push-static
  [offset]
  (concat
   (list (generate-static-label offset) "D=M")
   (push-sp)))

(defn pointer-addr [value] 
  (if (= "0" value) 
    "@THIS"
    "@THAT"
 ))

(defn push-pointer 
  [value]
  (concat
   (cons (pointer-addr value) '("D=M"))
   (push-sp)
))

(defn offsetable? [memory-sym] (contains?  #{"LCL" "ARG" "THIS" "THAT" "TEMP"} memory-sym))

(defn push-nonconstant-val
  [memory-sym offset]
  (cond
    (offsetable? memory-sym) (push-with-offset memory-sym offset)
    (= "PTR" memory-sym) (push-pointer offset)
    (= "STATIC" memory-sym) (push-static offset)
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
   (pop-sp)
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

(defn get-offset
  [memory-sym, offset]
  (concat
   (get-pointer-offset-pop memory-sym)
   (inc-addr offset)
   '("M=D")
))

(defn handle-static-offset-pop [offset] (list (generate-static-label offset) "M=D"))

(defn handle-pop-offset
  [memory-sym offset]
  (case memory-sym
    "PTR" (cons (pointer-addr offset)'("M=D"))
    "STATIC" (handle-static-offset-pop offset)
    (get-offset memory-sym offset) ; default
))



(defn pop-val
  [memory-sym offset]
  (concat
   (pop-sp)
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
