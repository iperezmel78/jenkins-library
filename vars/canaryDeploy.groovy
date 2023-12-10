#!/usr/bin/groovy

def call(project, app, version){
    sh "oc new-app -n ${project} --name ${app}-${version} ${app}:${version} -l app=${app},deploymentconfig=${app}-${version},hystrix.enabled=true"
    sh "oc set probe -n ${project} deployment/${app}-${version} --readiness --get-url=http://:8080/check"
    sh "oc patch svc/${app} -n ${project} -p '{\"spec\":{\"selector\":{\"app\": \"${app}\", \"deploymentconfig\": null}, \"sessionAffinity\":\"None\"}}' || echo 'Service ${app} already patched'"
    sh "oc delete service ${app}-${version} -n ${project}"
}
