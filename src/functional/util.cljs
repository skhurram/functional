(ns functional.util)

(defn log [& args]
  (.apply js/console.log js/console (apply array args)))
