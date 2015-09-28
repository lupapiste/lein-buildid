(ns leiningen.buildid
  (:require [clojure.java.shell :refer [sh]]
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
         :hg-branch (get-hg-branch)}
        (map (fn [e] [(-> e (.replace \_ \-) (.toLowerCase) (keyword)) (or (System/getenv e) "")]) environment-names)))

(defn buildid [project & args]
  (spit "./resources/buildid.edn" (make-buildinfo)))
