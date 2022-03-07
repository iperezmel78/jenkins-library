#!/usr/bin/groovy

def call(project, app){
    projectSet(project)
    sh "oc new-build -n ${project} --binary --name=${app} -l app=${app} --build-arg NPM_CA_FILE=nexus.crt || echo 'Build exists'"
    sh "oc start-build ${app} -n ${project} --from-dir=. --follow"
    deployApp(project, app, 'latest')
}
