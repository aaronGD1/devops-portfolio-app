pipeline {
    agent any

    environment {
        APP_NAME    = 'portfolio-app'
        APP_VERSION = '1.0.0'
        DEPLOY_PORT = '8081'
        JAR_FILE    = "target\\${APP_NAME}-${APP_VERSION}.jar"
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
                echo "Checking out source code from Git..."
                checkout scm
            }
        }

        stage('🔍 Code Quality Check') {
            steps {
                echo "Validating Maven project..."
                bat 'mvn validate'
            }
        }

        stage('📦 Build') {
            steps {
                echo "Compiling project..."
                bat 'mvn clean compile -DskipTests'
            }
        }

        stage('🧪 Unit Tests') {
            steps {
                echo "Running tests..."
                bat 'mvn test'
            }
        }

        stage('📊 Package') {
            steps {
                echo "Packaging application..."
                bat 'mvn package -DskipTests'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('🚀 Deploy') {
            steps {
                echo "Deploying application on port 8081..."

                bat '''
                echo Killing existing app if running on 8081...
                for /f "tokens=5" %%a in ('netstat -aon ^| findstr :8081') do taskkill /PID %%a /F

                echo Starting app...
                start cmd /c "java -jar target\\*.jar --server.port=8081"

                echo App started on port 8081
                '''
            }
        }
    }

    post {
        always {
            echo "Pipeline completed for ${APP_NAME}"
            cleanWs()
        }
        success {
            echo "🎉 SUCCESS — Application deployed on port 8081!"
        }
        failure {
            echo "🔥 FAILED — Check logs above"
        }
    }
}