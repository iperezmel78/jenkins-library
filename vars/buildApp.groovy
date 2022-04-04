#!/usr/bin/groovy

def call(project, app){
    projectSet(project)
    sh "oc new-build -n ${project} --binary --name=${app} -l app=${app} --build-arg NPM_AUTH=${NPM_AUTH} --build-arg NPM_CA_FILE=nexus.crt || echo 'Build exists'"
    sh "oc start-build ${app} -n ${project} --from-dir=. --follow"
    sh "oc patch dc/${app} -p '[{\"op\":\"add\",\"path\":\"/spec/template/spec/containers/0/envFrom\",\"value\":[{\"secretRef\":{\"name\":\"mysqldb\"}}]}]' --type=json"
    deployApp(project, app, 'latest')
}
