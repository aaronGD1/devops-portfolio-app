pipeline {
    agent any

    environment {
        APP_NAME    = 'portfolio-app'
        APP_VERSION = '1.0.0'
        DEPLOY_PORT = '8081'
        JAR_NAME    = 'portfolio-app-1.0.0.jar'   // ← exact jar name, no wildcard
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
            post {
                always {
                    junit testResults: 'target/surefire-reports/*.xml',
                          allowEmptyResults: true
                }
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
                echo "Deploying on port ${DEPLOY_PORT}..."

                // Step 1: Kill whatever is already on port 8081 (ignore errors if nothing running)
                bat '''
                    echo Stopping any existing process on port %DEPLOY_PORT%...
                    for /f "tokens=5" %%a in ('netstat -aon ^| findstr ":%DEPLOY_PORT% " ^| findstr LISTENING') do (
                        echo Killing PID %%a
                        taskkill /PID %%a /F
                    )
                    echo Port cleared.
                '''

                // Step 2: Resolve exact JAR path and write a launcher .bat file
                // Using a .bat launcher avoids wildcard issues entirely
                bat '''
                    echo Resolving JAR file...
                    for %%f in (target\\*.jar) do set JARFILE=%%f
                    echo Found JAR: %JARFILE%
                    echo java -jar %JARFILE% --server.port=%DEPLOY_PORT% > launcher.bat
                    echo Launcher created:
                    type launcher.bat
                '''

                // Step 3: Launch the app detached so Jenkins stage doesn't block
                bat '''
                    echo Starting Spring Boot app...
                    start "portfolio-app" /B cmd /c "launcher.bat > app.log 2>&1"
                    echo App launched. Waiting 15 seconds for startup...
                    timeout /t 15 /nobreak
                '''

                // Step 4: Verify the app is actually up
                bat '''
                    echo Verifying app is running on port %DEPLOY_PORT%...
                    curl -f http://localhost:%DEPLOY_PORT%/api/portfolio
                    if %ERRORLEVEL% NEQ 0 (
                        echo ERROR: App did not start correctly. Last 30 lines of log:
                        powershell -Command "Get-Content app.log -Tail 30"
                        exit /b 1
                    )
                    echo SUCCESS: App is live at http://localhost:%DEPLOY_PORT%
                '''
            }
        }
    }

    post {
        always {
            echo "Pipeline completed for ${APP_NAME} v${APP_VERSION}"
        }
        success {
            echo "🎉 SUCCESS — Portfolio live at http://localhost:${DEPLOY_PORT}"
        }
        failure {
            echo "❌ FAILED — Printing app.log for diagnosis:"
            bat 'powershell -Command "if (Test-Path app.log) { Get-Content app.log -Tail 50 }"'
        }
    }
}