(ns leiningen.buildid
  (:require [clojure.java.shell :refer [sh]]
            [clojure.java.io :as io]
            [clojure.string :as s]))

(def environment-names ["BUILD_NUMBER"
                        "BUILD_ID"
                        "BUILD_URL"
                        "NODE_NAME"
                        "JOB_NAME"
                        "BUILD_TAG"
                        "JENKINS_URL"
                        "EXECUTOR_NUMBER"
                        "JAVA_HOME"
                        "WORKSPACE"
                        "SVN_REVISION"
                        "CVS_BRANCH"
                        "GIT_COMMIT"
                        "GIT_URL"
                        "GIT_BRANCH"
                        "PROMOTED_URL"
                        "PROMOTED_JOB_NAME"
                        "PROMOTED_NUMBER"
                        "PROMOTED_ID"])

(defn ensure-dir! [dir-name]
  (let [dir (io/file dir-name)]
    (if (.exists dir)
      (.isDirectory dir)
      (.mkdir dir))))

(defn hg-repo? []
  (.isDirectory (io/file ".hg")))

(defn get-hg-branch []
  (let [r (sh "hg" "branch")]
    (if (zero? (:exit r))
      (s/trim (:out r))
      (str "error: " (:exit r) ": " (s/trim (:err r))))))

(defn hostname []
  (.getHostName (java.net.InetAddress/getLocalHost)))

(defn make-buildinfo []
  (into {:time (System/currentTimeMillis)
         :host (hostname)
         :hg-branch (if (hg-repo?) (get-hg-branch) "")}
        (map (fn [e] [(-> e (.replace \_ \-) (.toLowerCase) (keyword)) (or (System/getenv e) "")]) environment-names)))

(def filename "buildid.edn")

(defn buildid [project & args]
  (let [target-dir-name "resources"
        separator (java.io.File/separator)]
    (if (ensure-dir! target-dir-name)
      (spit (str target-dir-name separator filename) (make-buildinfo))
      (throw (Exception. (str target-dir-name " is not a directory!"))))))
