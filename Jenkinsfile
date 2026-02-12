pipeline {
    agent any

    options {
        disableConcurrentBuilds()
        timestamps()
    }

    environment {
        REPO_USERNAME = credentials('maven-credentials')
        REPO_PASSWORD = credentials('maven-credentials')
    }

    stages {
        stage('Checkout') {
            steps {
                scmSkip(
                    deleteBuild: true,
                    skipPattern: '.*\\[ci skip\\].*'
                )

                checkout scm
            }
        }

        stage('Build Project') {
            steps {
                sh 'chmod +x gradlew'
                script {
                    def buildCmd = "./gradlew build --no-daemon"
                    if (env.BUILD_NUMBER?.trim()) {
                        buildCmd += " -PbuildNumber=${env.BUILD_NUMBER}"
                    }
                    sh buildCmd
                }
            }
        }

        stage('Check Version') {
            steps {
                script {
                    def versionOutput = sh(script: "./gradlew -q printVersion", returnStdout: true).trim()
                    echo "Project version: ${versionOutput}"
                    env.PROJECT_VERSION = versionOutput

                    if (versionOutput.toUpperCase().contains("SNAPSHOT")) {
                        currentBuild.description = "<b>⚠ SNAPSHOT Build:</b> API may change between commits."
                        env.IS_SNAPSHOT = "true"
                    } else {
                        env.IS_SNAPSHOT = "false"
                    }
                }
            }
        }


    stage('Publish Snapshot') {
        when {
            expression { env.IS_SNAPSHOT == 'true' }
        }
        steps {
            withCredentials([usernamePassword(credentialsId: 'maven-credentials',
                                              usernameVariable: 'REPO_USER',
                                              passwordVariable: 'REPO_PASS')]) {
                sh '''
                    ./gradlew publish \
                        -PrepoUsername="$REPO_USER" \
                        -PrepoPassword="$REPO_PASS" \
                        --no-daemon
                '''
            }
            echo "Snapshot published successfully."
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
