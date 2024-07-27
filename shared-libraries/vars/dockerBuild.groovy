def call(String imageName, String dockerfilePath) {
    script {
        echo "Building Docker image..."
        sh "docker build -t ${imageName} -f ${dockerfilePath} ."
    }
}
