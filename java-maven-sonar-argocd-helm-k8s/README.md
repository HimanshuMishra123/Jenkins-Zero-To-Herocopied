# Jenkins Pipeline for Java based application using Maven, SonarQube, Argo CD, Helm and Kubernetes

![Screenshot 2023-03-28 at 9 38 09 PM](https://user-images.githubusercontent.com/43399466/228301952-abc02ca2-9942-4a67-8293-f76647b6f9d8.png)


Here are the step-by-step details to set up an end-to-end Jenkins pipeline for a Java application using SonarQube, Argo CD, Helm, and Kubernetes:

Prerequisites:

   -  Java application code hosted on a Git repository
   -   Jenkins server
   -  Kubernetes cluster
   -  Helm package manager
   -  Argo CD

Steps:

    1. Install the necessary Jenkins plugins:
       1.1 Git plugin
       1.2 Maven Integration plugin
       1.3 Docker Pipeline plugin
       1.4 Kubernetes Continuous Deploy plugin

    2. Create a new Jenkins pipeline:
       2.1 In Jenkins, create a new pipeline job and configure it with the Git repository URL for the Java application.
       2.2 Add a Jenkinsfile to the Git repository to define the pipeline stages.

    3. Define the pipeline stages:
        Stage 1: Checkout the source code from Git.
        Stage 2: Build the Java application using Maven.
        Stage 3: Run unit tests using JUnit and Mockito.
        Stage 4: Run SonarQube analysis to check the code quality.
        Stage 5: Package the application into a JAR file.
        Stage 6: Deploy the application to a test environment using Helm.
        Stage 7: Run user acceptance tests on the deployed application.
        Stage 8: Promote the application to a production environment using Argo CD.

    4. Configure Jenkins pipeline stages:
        Stage 1: Use the Git plugin to check out the source code from the Git repository.
        Stage 2: Use the Maven Integration plugin to build the Java application.
        Stage 3: Use the JUnit and Mockito plugins to run unit tests.
        Stage 4: Use the SonarQube plugin to analyze the code quality of the Java application.
        Stage 5: Use the Maven Integration plugin to package the application into a JAR file.
        Stage 6: Use the Kubernetes Continuous Deploy plugin to deploy the application to a test environment using Helm.
        Stage 7: Use a testing framework like Selenium to run user acceptance tests on the deployed application.
        Stage 8: Use Argo CD to promote the application to a production environment.

    5. Set up Argo CD:
        Install Argo CD on the Kubernetes cluster.
        Set up a Git repository for Argo CD to track the changes in the Helm charts and Kubernetes manifests.
        Create a Helm chart for the Java application that includes the Kubernetes manifests and Helm values.
        Add the Helm chart to the Git repository that Argo CD is tracking.

    6. Configure Jenkins pipeline to integrate with Argo CD:
       6.1 Add the Argo CD API token to Jenkins credentials.
       6.2 Update the Jenkins pipeline to include the Argo CD deployment stage.

    7. Run the Jenkins pipeline:
       7.1 Trigger the Jenkins pipeline to start the CI/CD process for the Java application.
       7.2 Monitor the pipeline stages and fix any issues that arise.

This end-to-end Jenkins pipeline will automate the entire CI/CD process for a Java application, from code checkout to production deployment, using popular tools like SonarQube, Argo CD, Helm, and Kubernetes.


### Architecture:

#### Introduction to Jenkins Pipeline

A Jenkins Pipeline is a suite of plugins that supports implementing and integrating continuous delivery pipelines into Jenkins. It allows defining the entire build process, from code commit to deployment, as declarative pipeline in groovy script. 

### Continuous Integration (CI) with Jenkins

#### Key Concepts
- **Continuous Integration (CI)** ensures that code changes are continuously tested and integrated into the main branch. This involves running unit tests, integration tests, and ensuring code quality.
- **Continuous Delivery (CD)** is the practice of automating the deployment of code to a staging or production environment.

#### Setting Up Jenkins with GitHub

1. **Git Repository**: The Java(or any other) codebase is stored in a Git repository.
2. **Triggering Jenkins**:
   - **Webhook**: Configuring a webhook in GitHub to notify Jenkins about commits or pull requests is the most efficient way to trigger Jenkins pipelines. This eliminates the need for Jenkins to continuously poll the repository, reducing unnecessary API calls.
   - **Configuration**: In GitHub settings, add the Jenkins webhook URL and specify the events that should trigger the webhook (e.g., commits, pull requests).

Example Configuration in GitHub:
```shell
# GitHub Webhook Settings
- Go to your GitHub repository settings
- Navigate to 'Webhooks'
- Add a new webhook with the Jenkins URL
- Select the events that will trigger the webhook
```
3. **Jenkins Pipeline Stages**:
   - **Build Stage**: **(you can skip the installations and configurations because Docker images  already contains all the necessary dependencies(example maven), configurations, and applications required for your workflow )
     - Uses Maven to build the Java application.
     - Executes unit tests during the build process.(Maven+ Maven surefire Plugin +Junit +Jacoco)
     - for unit test in build process include the Maven Surefire Plugin, JUnit, and JaCoCo dependencies in your pom.xml.
     - Skip the mvn clean install or mvn package commands if the code is already compiled and available in the Docker image.
     -Directly run tests and generate reports using:

     ```sh
     mvn test
     mvn surefire-report:report
     mvn jacoco:report
     ```
     These commands can be run in the Docker container, utilizing the pre-built environment.

     ** Maven surefire plugin acts as a bridge between Maven and the actual testing frameworks, such as JUnit or TestNG, which are the tools specifically designed for writing and running unit tests.<br/>
     ** The jacoco-maven-plugin collects coverage data during the tests and generates a report.<br/>

   - **Unit Testing**: Done with Build
   - **Static Code Analysis**:
     - Runs static code analysis tools (like SonarQube) to ensure no vulnerabilities are introduced.
   - **Security Analysis**:
     - Uses SAST (Static Application Security Testing) and DAST (Dynamic Application Security Testing) tools to check for security vulnerabilities.
   - **Functional Testing**: end to end testing 
   - **Reporting**: To generate reports of above steps
   - **Build Image**: A Docker image is created using shell commands.
   - **Push Image**: The Docker image is then pushed to a container registry (e.g., AWS ECR, Quay.io, Docker Hub).
4. **Notifications**:(use email extension plugin)
   - If any stage fails, notifications are sent via email or Slack.
   - If all stages are successful, the process moves to the next stage.

#### Jenkinsfile

A `Jenkinsfile` defines the pipeline and its stages. 

Example of a Declarative Jenkinsfile:
```groovy
pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                // Using Maven to build the application
                sh 'mvn clean package'
            }
        }
        stage('Test') {
            steps {
                // Running unit tests
                sh 'mvn test'
            }
        }
        stage('Static Code Analysis') {
            steps {
                // Running static code analysis
                sh 'mvn sonar:sonar'
            }
        }
        stage('Build Docker Image') {
            steps {
                // Building Docker image
                sh 'docker build -t myapp:latest .'
            }
        }
        stage('Push Docker Image') {
            steps {
                // Pushing Docker image to registry
                sh 'docker push myapp:latest'
            }
        }
    }
    post {
        always {
            // Notifications or cleanup steps
            mail to: 'dev-team@example.com',
                 subject: "Pipeline ${currentBuild.fullDisplayName}",
                 body: "Build finished with status: ${currentBuild.currentResult}"
        }
    }
}
```

#### Plugins and Tools
- **Maven Plugin**: Used for building Java applications.
- **Docker Agent**: Utilized to avoid local installations of tools. Docker images come pre-configured with required dependencies.
- **SonarQube**: For static code analysis to ensure code quality and security.
- **Email Extension Plugin**: For sending build notifications.


### Continuous Delivery (CD) with ArgoCD image updater, ArgoCD and Kubernetes 

#### CD Process

1. **Docker Image Deployment**:
   - After building the Docker image in the CI pipeline, push it to a Docker registry (e.g., Docker Hub, ECR).
   - Example Docker commands in Jenkinsfile:
     ```groovy
     stage('Build Docker Image') {
         steps {
             sh 'docker build -t myapp:latest .'
         }
     }
     stage('Push Docker Image') {
         steps {
             sh 'docker push myapp:latest'
         }
     }
     ```

2. **Kubernetes Deployment**:
   - Use tools like ArgoCD and Argo Image Updater to automate deployments to a Kubernetes cluster.
   - **Argo Image Updater**: Monitors the Docker registry for new images and updates the image in another Git repository dedicated having manifests(deployment.yml etc).
   - **ArgoCD**: Deploys the updated manifests from the Git repository to the Kubernetes cluster.

Example ArgoCD Configuration:
```yaml
apiVersion: argoproj.io/v1alpha1
kind: Application
metadata:
  name: myapp
spec:
  source:
    repoURL: 'https://github.com/your-org/your-repo'
    path: 'manifests'
    targetRevision: HEAD
  destination:
    server: 'https://kubernetes.default.svc'
    namespace: default
  project: default
```

---

### Summary of CI/CD Pipeline

- **CI Process**:
  - Developer commits code to Git.
  - GitHub webhook triggers Jenkins pipeline.
  - Jenkins pipeline runs build, test, and static analysis stages.
  - Docker image is built and pushed to a registry.

- **CD Process**:
  - Argo Image Updater detects new Docker images.
  - Updates manifests in a Git repository.
  - ArgoCD deploys the new application version to Kubernetes.

By following these steps, you can set up an efficient and scalable CI/CD pipeline using Jenkins, Docker, and Kubernetes with tools like ArgoCD. This approach ensures a streamlined process from code commit to production deployment, enhancing productivity and reliability.


## Tools and Technologies

- **Jenkins**: For CI/CD pipeline orchestration.
- **Maven**: For building the Java application.
- **SonarQube**: For static code analysis.
- **SAST and DAST tools**: For security analysis.
- **Docker**: For containerizing the application.
- **Container Registry**: AWS ECR, Quay.io, Docker Hub for storing Docker images.
- **Kubernetes**: For container orchestration.
- **Argo Image Updater**: For updating image manifests in the Git repository.
- **Argo CD**: For deploying applications to Kubernetes based on Git repository changes.