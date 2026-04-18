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

                // Step 1: Kill anything already on port 8081
                bat '''
                    echo Stopping any existing process on port %DEPLOY_PORT%...
                    for /f "tokens=5" %%a in ('netstat -aon ^| findstr ":%DEPLOY_PORT% " ^| findstr LISTENING') do (
                        echo Killing PID %%a
                        taskkill /PID %%a /F
                    )
                    echo Port cleared.
                '''

                // Step 2: Resolve exact JAR name, write launcher.bat
                bat '''
                    echo Resolving JAR file...
                    for %%f in (target\\*.jar) do set JARFILE=%%f
                    echo Found JAR: %JARFILE%
                    echo java -jar %JARFILE% --server.port=%DEPLOY_PORT% > launcher.bat
                    echo Launcher created:
                    type launcher.bat
                '''

                // Step 3: Launch detached
                // IMPORTANT: Use "ping -n 31 127.0.0.1" instead of "timeout /t 30"
                // because Jenkins redirects stdin and Windows timeout.exe crashes on redirected stdin.
                // ping -n 31 sends 31 ICMP packets 1 second apart = ~30 second wait, no stdin needed.
                bat '''
                    echo Starting Spring Boot app...
                    start "portfolio-app" /B cmd /c "launcher.bat > app.log 2>&1"
                    echo App process launched. Waiting 30 seconds for startup...
                    ping -n 31 127.0.0.1 > nul
                    echo Done waiting.
                '''

                // Step 4: Verify app is up using PowerShell (more reliable than curl on Windows)
                bat '''
                    echo Verifying app is running on port %DEPLOY_PORT%...
                    powershell -Command "try { $r = Invoke-WebRequest -Uri 'http://localhost:%DEPLOY_PORT%/' -UseBasicParsing -TimeoutSec 10; Write-Host 'HTTP Status:' $r.StatusCode; exit 0 } catch { Write-Host 'App not responding:' $_.Exception.Message; exit 1 }"
                    if %ERRORLEVEL% NEQ 0 (
                        echo ERROR: App did not respond. Last 40 lines of app.log:
                        powershell -Command "Get-Content app.log -Tail 40"
                        exit /b 1
                    )
                    echo SUCCESS: Portfolio app is live at http://localhost:%DEPLOY_PORT%
                '''
            }
        }
    }

    post {
        always {
            echo "Pipeline finished for ${APP_NAME} v${APP_VERSION}"
        }
        success {
            echo "🎉 SUCCESS — Portfolio live at http://localhost:${DEPLOY_PORT}"
        }
        failure {
            echo "❌ FAILED — printing app.log:"
            bat 'powershell -Command "if (Test-Path app.log) { Get-Content app.log -Tail 50 }"'
        }
    }
}