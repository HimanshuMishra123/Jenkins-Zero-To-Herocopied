def call(String imageName, String trivyReportFile) {
    script {
        echo "Running Trivy scan..."
        sh "trivy image --format json --output ${trivyReportFile} ${imageName}"
    }
}
