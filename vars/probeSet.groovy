def call(project, app){
    sh "oc set probe dc/${app} -n ${project} --readiness --get-url=http://:8080/check"
    sh "oc set probe dc/${app} -n ${project} --liveness --get-url=http://:8080/check"
}
