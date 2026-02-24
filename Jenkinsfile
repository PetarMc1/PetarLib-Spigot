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

        stage('Check Version') {
            steps {
                script {
                    sh 'chmod +x gradlew'
                    def versionOutput = sh(script: "./gradlew -q printVersion", returnStdout: true).trim()
                    echo "Project version: ${versionOutput}"
                    env.PROJECT_VERSION = versionOutput

                    if (versionOutput.toUpperCase().contains("SNAPSHOT")) {
                        currentBuild.description = "**⚠ SNAPSHOT Build:** API may change between commits."
                        env.IS_SNAPSHOT = "true"
                    } else {
                        env.IS_SNAPSHOT = "false"
                    }
                }
            }
        }

        stage('Run Tests') {
            steps {
                sh './gradlew test --no-daemon'
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


        stage('Build Project') {
            steps {
                script {
                    def buildCmd = "./gradlew clean shadowJar --no-daemon"
                    if (env.BUILD_NUMBER?.trim()) {
                        buildCmd += " -PbuildNumber=${env.BUILD_NUMBER}"
                    }
                    sh buildCmd
                }
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
