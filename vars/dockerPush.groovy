def call(String imageName, String dockerRegistry) {
    script {
        echo "Pushing Docker image..."
        sh "docker push ${dockerRegistry}/${imageName}"
    }
}
