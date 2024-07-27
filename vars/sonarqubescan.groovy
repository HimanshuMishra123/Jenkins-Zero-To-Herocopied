def call(String projectPath, String sonarCredentialsId, String sonarUrlCredentialsId) {
    def sonarUrl = ''
    
    // Retrieve SonarQube URL from credentials
    withCredentials([string(credentialsId: sonarUrlCredentialsId, variable: 'SONAR_URL')]) {
        sonarUrl = env.SONAR_URL
    }

    withCredentials([string(credentialsId: sonarCredentialsId, variable: 'SONAR_AUTH_TOKEN')]) {
        script {
            echo "Running SonarQube scan..."
            sh "cd ${projectPath} && mvn sonar:sonar -Dsonar.login=$SONAR_AUTH_TOKEN -Dsonar.host.url=${sonarUrl}"
        }
    }
}
