pipeline {
    agent any

    environment {
        APP_NAME    = 'portfolio-app'
        APP_VERSION = '1.0.0'
        DEPLOY_PORT = '8081'
    }

    tools {
        maven 'Maven-3.9'
        jdk   'JDK-17'
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timeout(time: 20, unit: 'MINUTES')
        timestamps()
    }

    stages {

        stage('📥 Checkout') {
            steps {
                echo "Checking out code..."
                checkout scm
            }
        }

        stage('🔍 Validate') {
            steps {
                bat 'mvn validate'
            }
        }

        stage('📦 Build') {
            steps {
                bat 'mvn clean compile -DskipTests'
            }
        }

        stage('🧪 Test') {
            steps {
                bat 'mvn test'
            }
        }

        stage('📊 Package') {
            steps {
                bat 'mvn package -DskipTests'
                archiveArtifacts artifacts: 'target/*.jar'
            }
        }

        stage('🚀 Deploy') {
            steps {
                echo "Deploying application on port 8081..."

                bat '''
                echo Killing existing app on port 8081...
                for /f "tokens=5" %%a in ('netstat -aon ^| findstr :8081') do taskkill /PID %%a /F

                echo Starting application...
                start cmd /k "java -jar target\\*.jar --server.port=8081"
                '''
            }
        }
    }

    post {
        always {
            echo "Pipeline completed"
        }
        success {
            echo "🎉 SUCCESS — App deployed on port 8081!"
        }
        failure {
            echo "❌ FAILED — Check logs"
        }
    }
}