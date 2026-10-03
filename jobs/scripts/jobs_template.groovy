timeout(5) {
    node("jenkins-jenkins-agent") {
        stage("Checkout") {
            checkout scm
        }
        stage("Deploy changes to jenkins") {
            dir("jobs") {
                //Не заработает если на агент не накатывали бинарь jenkins-jobs
                sh 'pip3 install --user jenkins-job-builder'
                sh "jenkins-jobs --conf ./jobs.ini update ./jobs"
            }
        }
    }
}