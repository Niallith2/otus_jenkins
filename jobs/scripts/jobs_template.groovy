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
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Deploy changes to jenkins') {
            steps {
                container('python') {
                    dir('jobs') {
                        sh '''
                            pip install --user jenkins-job-builder
                            ~/.local/bin/jenkins-jobs --conf $WORKSPACE/jenkins_jobs.ini update .
                        '''
                    }
                }
            }
        }
    }
}
