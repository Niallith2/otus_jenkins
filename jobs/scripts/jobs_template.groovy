pipeline {
    agent {
        kubernetes {
            cloud 'otus'
            yaml '''
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: jnlp
    image: jenkins/inbound-agent:3385.vf1123fb_515da_-1
  - name: python
    image: python:3.12-slim
    command: ["cat"]
    tty: true
'''
        }
    }
    triggers {
        pollSCM('H/5 * * * *')  // проверка каждые 5 минут (можно настроить)
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Deploy changes to jenkins') {
            steps {
                container('python') {
                    sh '''
                            pip install --user "setuptools<82.0.0"
                            pip install --user jenkins-job-builder
                            ls ~/.local/bin/jenkins-jobs
                            ls
                            ls $WORKSPACE
                            ~/.local/bin/jenkins-jobs --conf $WORKSPACE/jobs.ini update $WORKSPACE/jobs
                        '''
                }
            }
        }
    }
}
