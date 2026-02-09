pipeline {
    agent any

    options {
        disableConcurrentBuilds()
        timestamps()
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Project') {
            steps {
                sh 'chmod +x gradlew'
                script {
                    def buildCmd = "./gradlew jar --no-daemon"
                    if (env.BUILD_NUMBER?.trim()) {
                        buildCmd += " -PbuildNumber=${env.BUILD_NUMBER}"
                    }
                    echo "Running: ${buildCmd}"
                    sh buildCmd
                }
            }
        }

        stage('List Build Artifacts') {
            steps {
                echo "Artifacts in build/libs:"
                sh 'ls -R build/libs'
            }
        }

        stage('Archive Build Artifacts') {
            steps {
                archiveArtifacts artifacts: 'build/libs/**/*.jar', fingerprint: true
            }
        }

    }

    post {
        always {
            cleanWs()
        }
    }
}
