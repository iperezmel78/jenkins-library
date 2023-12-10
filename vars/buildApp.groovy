#!/usr/bin/groovy

def call(project, app){
    sh "oc new-build -n ${project} --binary --name=${app} -l app=${app} --build-arg NPM_AUTH=${NPM_AUTH} --build-arg NPM_CA_FILE=nexus.crt || echo 'Build exists'"
    sh "oc start-build ${app} -n ${project} --from-dir=. --follow"
}
