pipeline {
    agent any

    environment {
        APP_NAME        = 'portfolio-app'
        APP_VERSION     = '1.0.0'
        JAVA_HOME       = '/usr/lib/jvm/java-17-openjdk'
        MAVEN_HOME      = '/usr/share/maven'
        DEPLOY_PORT     = '8080'
        JAR_FILE        = "target/${APP_NAME}-${APP_VERSION}.jar"
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
                echo "Branch: ${env.GIT_BRANCH}"
                echo "Commit: ${env.GIT_COMMIT}"
            }
        }

        stage('🔍 Code Quality Check') {
            steps {
                echo "Validating Maven project structure..."
                sh 'mvn validate'
            }
        }

        stage('📦 Build') {
            steps {
                echo "Compiling source code with Maven..."
                sh 'mvn clean compile -DskipTests'
            }
            post {
                success { echo "✅ Build successful!" }
                failure { echo "❌ Build failed!" }
            }
        }

        stage('🧪 Unit Tests') {
            steps {
                echo "Running unit tests..."
                sh 'mvn test'
            }
            post {
                always {
                    junit testResults: 'target/surefire-reports/*.xml',
                          allowEmptyResults: true
                    echo "Test reports published."
                }
                success { echo "✅ All tests passed!" }
                failure { echo "❌ Tests failed! Check reports." }
            }
        }

        stage('📊 Package') {
            steps {
                echo "Packaging application as JAR..."
                sh 'mvn package -DskipTests'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                echo "Artifact archived: ${JAR_FILE}"
            }
        }

        stage('🚀 Deploy') {
            when {
                branch 'main'
            }
            steps {
                echo "Deploying ${APP_NAME} v${APP_VERSION} to server..."
                sh '''
                    # Stop existing instance if running
                    PID=$(lsof -t -i:8080 || true)
                    if [ -n "$PID" ]; then
                        echo "Stopping existing process on port 8080 (PID: $PID)"
                        kill -9 $PID || true
                        sleep 3
                    fi

                    # Start new instance
                    echo "Starting new instance..."
                    nohup java -jar target/*.jar \
                        --server.port=8080 \
                        --spring.profiles.active=prod \
                        > app.log 2>&1 &

                    echo "Application started. PID: $!"

                    # Wait for app to start
                    echo "Waiting for application to become ready..."
                    for i in $(seq 1 30); do
                        if curl -sf http://localhost:8080/api/portfolio > /dev/null 2>&1; then
                            echo "✅ Application is up and running on port 8080!"
                            exit 0
                        fi
                        echo "  Attempt $i/30 — waiting..."
                        sleep 3
                    done
                    echo "⚠️  Application may not have started correctly. Check app.log"
                '''
            }
        }
    }

    post {
        always {
            echo "Pipeline completed for ${APP_NAME}"
            cleanWs(cleanWhenNotBuilt: false,
                    deleteDirs: true,
                    disableDeferredWipeout: true,
                    notFailBuild: true,
                    patterns: [[pattern: '.gitignore', type: 'INCLUDE'],
                                [pattern: 'target/', type: 'EXCLUDE']])
        }
        success {
            echo "🎉 Pipeline SUCCESS — ${APP_NAME} v${APP_VERSION} deployed!"
        }
        failure {
            echo "🔥 Pipeline FAILED — Check logs above for details."
        }
        unstable {
            echo "⚠️  Pipeline UNSTABLE — Tests may have failed."
        }
    }
}
