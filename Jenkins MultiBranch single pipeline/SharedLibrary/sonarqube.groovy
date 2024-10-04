//This function will contain the commands required to run a SonarQube analysis. 
// It typically involves invoking the SonarQube scanner with the appropriate parameters.

def runSonarQube() {
    echo 'Running SonarQube Analysis for Release Branch...'
    
    // Set environment variables needed for SonarQube (if any)
    withSonarQubeEnv('SonarQube') {
        // The command below assumes you're using Maven, but you can adjust it based on your build tool (e.g., Gradle, npm)
        sh "mvn sonar:sonar -Dsonar.projectKey=your-project-key \
                           -Dsonar.host.url=http://your-sonarqube-server \
                           -Dsonar.login=your-sonarqube-token"
    }

    // Optionally, wait for SonarQube quality gate result
    timeout(time: 5, unit: 'MINUTES') {
        def qualityGate = waitForQualityGate()
        if (qualityGate.status != 'OK') {
            error "Pipeline aborted due to SonarQube quality gate failure: ${qualityGate.status}"
        }
    }
}


// withSonarQubeEnv('SonarQube'): Sets up the environment for the SonarQube server configured in Jenkins.
// sh "mvn sonar:sonar": Runs the SonarQube analysis (assumes you're using Maven as the build tool, but you can change it based on your setup).
// waitForQualityGate(): This checks whether the SonarQube analysis meets the defined quality gate (e.g., passing code coverage, no critical issues). If it fails, the pipeline is aborted.