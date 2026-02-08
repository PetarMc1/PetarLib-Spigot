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

        stage('Build All Versions') {
            steps {
                sh 'chmod +x gradlew'
                script {
                    def buildCmd = "./gradlew build --no-daemon"
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
                echo "Artifacts in build/libs-versioned:"
                sh 'ls -R build/libs-versioned'
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
