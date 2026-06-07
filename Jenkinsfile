pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                checkout([$class: 'GitSCM',
                          branches: [[name: 'refs/heads/main']],
                          userRemoteConfigs: [[url: 'https://github.com/VijayAndroidTest/NaguOrg17.4.2025.git']]])
            }
        }
        stage('Test') {
            steps {
                bat 'gradlew.bat testDebugUnitTest'
            }
        }
        stage('Build') {
            steps {
                withCredentials([
                        string(credentialsId: 'keystore-password', variable: 'KEYSTORE_PASSWORD'),
                        string(credentialsId: 'key-password', variable: 'KEY_PASSWORD')
                ]) {
                    // This uses the 'release' signing config defined in gradle
                    bat 'gradlew.bat bundleRelease'
                }
            }
        }
        stage('Distribute Release') {
            steps {
                withCredentials([file(credentialsId: 'firebase-service-json', variable: 'SERVICE_ACCOUNT_FILE')]) {
                    // Distribute the signed release build
                    bat 'set GOOGLE_APPLICATION_CREDENTIALS=%SERVICE_ACCOUNT_FILE% && gradlew.bat appDistributionUploadRelease -PappDistributionGroups=testers'
                }
            }
        }
    }
    post {
        always {
            junit 'app/build/test-results/**/*.xml'
        }
    }
}