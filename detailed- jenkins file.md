

### Jenkinsfile with lots of tools
#### 
```groovy
pipeline {
    agent any

    environment {
        SONARQUBE_URL = credentials('sonarqube-url') // Stored as a Jenkins credential
        SONARQUBE_TOKEN = credentials('sonarqube-token') // SonarQube token stored in Jenkins credentials
        CHECKMARX_CREDENTIALS = credentials('checkmarx-credentials') // Checkmarx credentials stored in Jenkins credentials
        DOCKER_REGISTRY = 'mydockerhub'
        IMAGE_NAME = 'myapp:latest'
        TRIVY_REPORT_FILE = 'trivy-report.json'
        SELENIUM_GRID_URL = 'http://localhost:4444/wd/hub' // URL for Selenium Grid or local server
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build') {
            steps {
                echo "Building project..."
                sh 'mvn clean install'
            }
        }
        stage('Unit & Integration Tests with TestNG') {
            steps {
                echo "Running Unit and Integration tests..."
                sh 'mvn test'
                // Collect TestNG reports
                publishTestNGResults testResultsPattern: '**/target/testng-results.xml'
            }
        }
        stage('End-to-End Tests with Selenium') {
            steps {
                // script way of writing jenkins file concept is used when you use jenkins configure system(manage jenkins>>configure system) to configure the details of plugins installed(exaple: sonar url etc.), but it is suggested to use credentials if the data is to be kept secret.
                script {
                    echo "Running End-to-End tests with Selenium..."
                    sh """
                    mvn verify -Dselenium.grid.url=${SELENIUM_GRID_URL} -P e2e-tests
                    """
                }
            }
            post {
                always {
                    archiveArtifacts artifacts: '**/target/*.xml', allowEmptyArchive: true
                }
            }
        }
        stage('Static Code Analysis with SonarQube') {
            steps {
                script {
                    withSonarQubeEnv('SonarQube') {
                        sh "mvn sonar:sonar -Dsonar.host.url=$SONARQUBE_URL -Dsonar.login=$SONARQUBE_TOKEN"
                    }
                }
            }
        }
        stage('SAST with Checkmarx') {
            steps {
                script {
                    echo "Running Checkmarx SAST scan..."
                    // Placeholder for Checkmarx CLI or API integration
                    sh 'checkmarx scan --project myProject --username $CHECKMARX_CREDENTIALS_USR --password $CHECKMARX_CREDENTIALS_PSW'
                }
            }
        }
        stage('Image Build') {
            steps {
                script {
                    sh "docker build -t ${IMAGE_NAME} ."
                }
            }
        }
        stage('Image Scanning with Trivy') {
            steps {
                script {
                    sh "trivy image --format json --output ${TRIVY_REPORT_FILE} ${IMAGE_NAME}"
                }
            }
        }
        stage('DAST with OWASP ZAP') {
            steps {
                script {
                    echo "Running OWASP ZAP for DAST..."
                    sh """
                    docker run -v \$(pwd):/zap/wrk/:rw --network="host" owasp/zap2docker-stable zap-baseline.py -t http://localhost:8080 -r zap-report.html
                    """
                }
            }
            post {
                always {
                    archiveArtifacts artifacts: 'zap-report.html', allowEmptyArchive: true
                }
            }
        }
        stage('Image Push') {
            steps {
                script {
                    sh "docker push ${DOCKER_REGISTRY}/${IMAGE_NAME}"
                }
            }
        }
    }

    post {
        always {
            echo "Pipeline completed."
        }
        success {
            echo "Pipeline succeeded."
        }
        failure {
            echo "Pipeline failed."
        }
    }
}
```

### Key Changes:
- **Unit & Integration Tests with TestNG**:
  - Uses `publishTestNGResults` instead of `junit` to handle TestNG results. This plugin specifically handles TestNG reports.
  - The `testResultsPattern` should match the location where TestNG results are generated (typically `**/target/testng-results.xml`).

- **End-to-End Tests**:
  - The `archiveArtifacts` step collects all XML files in the target directory. Adjust this if your reports are stored elsewhere.

This setup ensures that TestNG results are processed correctly and visible in Jenkins, aligning with the use of TestNG for testing.