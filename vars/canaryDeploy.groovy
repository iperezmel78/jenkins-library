#!/usr/bin/groovy

def call(project, app, version){
    sh "oc new-app -n ${project} --name ${app}-${version} ${app}:${version} -l app=${app},deploymentconfig=${app}-${version},hystrix.enabled=true"
    sh "oc set probe -n ${project} dc/${app}-${version} --readiness --get-url=http://:8080/check"
    sh "oc patch svc/${app} -n ${project} -p '{\"spec\":{\"selector\":{\"app\": \"${app}\", \"deploymentconfig\": null}, \"sessionAffinity\":\"None\"}}' || echo 'Service ${app} already patched'"
    sh "oc delete service ${app}-${version} -n ${project}"
    sh "oc patch dc/${app}-${version} -p '[{\"op\":\"add\",\"path\":\"/spec/template/spec/containers/0/envFrom\",\"value\":[{\"secretRef\":{\"name\":\"mysqldb\"}}]}]' --type=json"
}
