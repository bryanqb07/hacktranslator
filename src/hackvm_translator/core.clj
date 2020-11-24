(ns hackvm-translator.core
  (:require [hackvm-translator.parser :as parser]
            [hackvm-translator.generator :as coder]
            [clojure.string :as s])
  (:gen-class))


(def filename "PointerTest.vm")

(defn -asm-file-name
  [filename]
  (str (first (s/split filename #"\.")) ".asm"))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (coder/spit-to-file (-asm-file-name filename) (parser/get-commands-from-file filename)))
