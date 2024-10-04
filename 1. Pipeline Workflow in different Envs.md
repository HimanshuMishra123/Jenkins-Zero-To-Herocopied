![CI_CD Workflow from Dev to Stage to Prod Environments_ Complete CI_CD Process _#abhishekveeramalla 6-50 screenshot](https://github.com/user-attachments/assets/f6bf4bcd-f0f5-4582-b7ea-7c7a5209b568)

## Jenkins CI/CD Workflow Overview

### Introduction
- **Objective**: Explain the complete workflow involved in moving an application from development to production through various environments such as Staging, QA, Pre-Production, and Production.
- **Platforms Used**: Jenkins, GitHub, Kubernetes, Argo CD, Docker, SonarQube, Trivy.

### Continuous Integration (CI) Pipeline
1. **Triggering the Pipeline**:
   - A webhook in GitHub triggers the Jenkins pipeline when a user commits changes to a feature branch.

2. **Stages in CI Pipeline**:
   - **Code Checkout**: Retrieve the code from the GitHub repository.
   - **Build and Unit Testing**: Use tools like Maven for Java applications to compile the code and run unit tests.
   - **Code Quality Analysis**: Analyze the code for issues using tools like SonarQube.
   - **Container Image Creation**: Create Docker images for the application.
   - **Image Scanning**: Use tools like Trivy or Clair to scan the Docker image for vulnerabilities.
   - **Push to Image Registry**: Upload the Docker image to a registry such as Docker Hub or Artifactory.

3. **Kubernetes Manifest Update**:
   - Update the Kubernetes deployment manifests (e.g., `deployment.yaml`) with the new image version.
   - Use a separate Git repository for manifests.
   - Optionally,you can  use Helm charts for deployment and update the `values.yaml` file in manifest repo.

4. **Deployment to Development Environment**:
   - Deploy the application to the development environment's Kubernetes cluster using gitops tools like Argo CD, Flux, or Spinnaker.

![CI_CD Workflow from Dev to Stage to Prod Environments_ Complete CI_CD Process _#abhishekveeramalla 13-20 screenshot](https://github.com/user-attachments/assets/54e99a42-941e-4595-9dfc-456df367a1db)

**In companies there will 100s of microservices(example: amazon.com) and for each microservice(example: login, payments, cart etc. ) there will be seprate repositories(examp payments repositary) and each repo will have several branches(feature, main, release and hotfix).**


### Branching Strategy
- **Feature Branches**: Separate branches for new features or enhancements.
- **Main Branch**: The branch where active development occurs, integrating changes from various feature branches.Earlier main branch was called master branch but now main branch. on main active development occurs which are some fixes or minor enhancements to the existing code already live to the customers. All of these are considered active development, always ongoing in the main branch
- **Release Branches**: Branches used to prepare for releases, containing stable and tested code.
- **Hotfix Branches**: Special branches for critical fixes in production env.
For example, if there's a critical bug in the iOS 15 release, a hotfix branch is created, and changes are merged into the main, feature, and release branches.

![image](https://github.com/user-attachments/assets/74462d87-0227-4638-a0fe-d99e2c08b6af)

### Promoting Code to Staging Environment
1. **Feature Branch to Main Branch**:
   - Once user has committed a change to the feature branch it will trigger the Jenkins Pipeline and it runs all the stages and finally deploys on one of the kubernetes Clusters(development environment) that is assigned for this feature branch.
   - After changes are verified in the development environment(very particular to a
   specific feature) also found stable and reviewed by peers, they are merged into the main branch.
   - Feature branch is only particular to a specific change but once it is merged to main branch it will be integrated with all the other changes that the entire company is working on or the entire project team is working on so it is very very important to make sure that your feature Branch not only works for your changes on the kubernetes cluster in the developer environment but it is well integrated with the other changes that went into the main branch. 
   - A different Jenkins pipeline triggers, deploying the application to a staging environment.
   - Main Branch Jenkins pipeline will have a lot of functional tests or regression test or automated tests
   

2. **Staging Environment**:(also called QA branch as owned by testing team)
   - Here QA team owns the staging environment that's why it is also called as QA environment in some organizations.
   - QA team will run additional manual tests here like performance tests,DDOS attack tests, penetration testing.
   - QA team takes good amount of time(3-5 days) and they will confirm that okay your application is working fine on the QA environment
   - Like this there will be other feature branches as well merging to Main branch and QA team will keep testing the changes on the QA environment. when QA team provides a sign off that all the features that you want to take to a release are working fine on the staging environment.Your development team along with your product owner and everyone decides that okay we are ready for the release.
   

### Release Process
1. **Creating a Release Branch**:
   - A release branch is created by Devops engineer from the main branch once the staging environment tests are successful which will trigger another Jenkins pipeline.
   - For a pre-production environment you can add little more stages in Release branch jenkins pipeline like automated tests, pen testing, performance testing. Before promoting your application you can run these tests because these take a lot of time so for development environment and staging environment you can ignore these tests on each and every commit.

2. **Pre-Production Deployment**:
   - Deploy the application to a pre-production(some ppl call it pre-prod or UAT) environment k8s cluster using Argocd from the release branch.
   - Perform additional tests if necessary, such as integration testing, load/stress testing and security testing.

3. **Production Deployment**:
   - Upon successful testing in the pre-production environment for some time, the application is deployed to the production environment from the same release pipeline.
   - Best practice includes using separate Kubernetes clusters for development, staging, pre-production, and production to isolate environments.



### Best Practices
- **Separate Environments**: Use different Kubernetes clusters or namespaces for each environment to prevent interference.
- **Automated Tests**: Integrate automated tests at each stage to ensure code quality and functionality.
- **Security Considerations**: Implement image scanning and penetration testing, especially before production deployment.
- **Monitoring and Logging**: Set up monitoring and logging in all environments to track performance and identify issues.<br/>

*** nobody will have access to the production cluster whereas development teams and QE team will only have access to Development and staging environement only.<br/>

**pre-production is basically used for debugging any issues on the production environment**  let's say someone says something is not working on the production environment you will immediately not log into the production environment and debug issues there but instead you can try to reproduce that kind of issue on a pre-production environment and try to resolve it. there can be multiple customers reporting different sort of issues so it is ideal to reproduce those issues on pre-production environment and work on that.<br/>

![image](https://github.com/user-attachments/assets/643f0452-7f9a-4279-a8b6-8fadf574d8ae)


**Jenkins multibranch feature** allows you to streamline the CI/CD process by using a single pipeline configuration to manage multiple branches in a project. Instead of creating separate pipelines for each branch, you can define one pipeline that can dynamically discover branches and execute the appropriate stages based on the branch being built.

This approach simplifies the management of Jenkins pipelines by reducing the duplication of pipeline configurations and enabling easier maintenance and updates. It also supports the automation of workflows for different environments (such as development, staging, and production) by defining different stages within the pipeline that correspond to each environment.

In the context of Jenkins multibranch pipelines, you can also use tools like Argo CD to manage deployments across different environments. By using different folders or Helm charts in your Git repository for each environment (development, staging, pre-production, and production), you can set up Argo CD to monitor these folders and deploy the applications accordingly. This setup ensures a consistent deployment process across all environments and allows for easy scaling and management of your applications.

This method not only simplifies the pipeline setup but also ensures that the CI/CD process is more streamlined and efficient, reducing the need for multiple pipelines and providing a clear and organized approach to managing code changes across various environments.
