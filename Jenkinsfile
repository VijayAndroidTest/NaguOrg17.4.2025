pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                checkout([$class: 'GitSCM',
                          branches: [[name: 'refs/heads/master']],
                          userRemoteConfigs: [[url: 'https://github.com/VijayAndroidTest/NaguOrg.git']]])
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
    }
    // This post block runs after the stages, even if they fail
    post {
        always {
            // This collects the test results and displays them in Jenkins
            junit 'app/build/test-results/**/*.xml'
        }
    }
}