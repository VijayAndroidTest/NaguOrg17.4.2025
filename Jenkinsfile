pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                // Change 'refs/heads/master' to 'refs/heads/main'
                checkout([$class: 'GitSCM',
                          branches: [[name: 'refs/heads/main']],
                          userRemoteConfigs: [[url: 'https://github.com/VijayAndroidTest/NaguOrg17.4.2025.git']]])
            }
        }
        stage('Test') {
            steps {
                // Run the unit tests
                bat 'gradlew.bat testDebugUnitTest'
            }
        }
        stage('Build') {
            steps {
                bat 'gradlew.bat assembleDebug'
            }
        }
        stage('Archive APK') {
            steps {
                archiveArtifacts artifacts: 'app/build/outputs/apk/debug/*.apk', fingerprint: true
            }
        }
        stage('Distribute') {
            steps {
                withCredentials([file(credentialsId: 'firebase-service-json', variable: 'SERVICE_ACCOUNT_FILE')]) {
                    // Replace 'nagu-testers' with your actual group alias from Firebase
                    bat "gradlew appDistributionUploadRelease -PappDistributionServiceCredentialsFile=${SERVICE_ACCOUNT_FILE} -PappDistributionGroups=testers"
                }
            }
        }
    }
    // This post block runs after the stages, even if they fail
    post {
        always {
            // This collects the test results and displays them in Jenkins
            junit 'app/build/test-results/**/*.xml'
        }
    }
}