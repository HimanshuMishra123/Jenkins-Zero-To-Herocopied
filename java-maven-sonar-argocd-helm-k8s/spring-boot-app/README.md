# Spring Boot based Java web application
 
This is a simple Sprint Boot based Java application that can be built using Maven. Sprint Boot dependencies are handled using the pom.xml 
at the root directory of the repository.

This is a MVC architecture based application where controller returns a page with title and message attributes to the view.

## Execute the application locally and access it using your browser

Checkout the repo and move to the directory

```
git clone https://github.com/iam-veeramalla/Jenkins-Zero-To-Hero/java-maven-sonar-argocd-helm-k8s/sprint-boot-app
cd java-maven-sonar-argocd-helm-k8s/sprint-boot-app
```

Execute the Maven targets to generate the artifacts

```
mvn clean package
```
The command `mvn clean package` is a Maven command used in Java projects to clean and build the project. Here's a simple explanation of what it does:

1. **clean**: This step deletes any files generated from previous builds. It cleans up the `target` directory, which typically contains compiled code, previous build outputs, and other temporary files. This ensures that the next build is fresh and not affected by any leftover artifacts from previous builds.

2. **package**: This step compiles the source code, runs tests, and packages the compiled code into a deliverable format, such as a JAR (Java Archive) or WAR (Web Archive) file, which can be deployed. This packaged file is placed in the `target` directory.

In summary, `mvn clean package` first cleans the project and then builds it, producing a fresh package for deployment.

The above maven target stroes the artifacts to the `target` directory. You can either execute the artifact on your local machine
(or) run it as a Docker container.

** Note: To avoid issues with local setup, Java versions and other dependencies, I would recommend the docker way. **


### Execute locally (Java 11 needed) and access the application on http://localhost:8080

```
java -jar target/spring-boot-web.jar
```

### The Docker way

Build the Docker Image

```
docker build -t ultimate-cicd-pipeline:v1 .
```

```
docker run -d -p 8010:8080 -t ultimate-cicd-pipeline:v1
```

Hurray !! Access the application on `http://<ip-address>:8010`


## Next Steps

### Configure a Sonar Server locally
apply command one by one.
```
apt install unzip
adduser sonarqube
wget https://binaries.sonarsource.com/Distribution/sonarqube/sonarqube-9.4.0.54424.zip
unzip *
chmod -R 755 /home/sonarqube/sonarqube-9.4.0.54424
chown -R sonarqube:sonarqube /home/sonarqube/sonarqube-9.4.0.54424
cd sonarqube-9.4.0.54424/bin/linux-x86-64/
./sonar.sh start
```
if unzip* fails `sudo apt install unzip` 
`cd sonarqube-9.4.0.54424/bin` if you are not sure about architecture then cd to your arch. example `cd sonarqube-9.4.0.54424/bin/linux-x86-64/`

Hurray !! Now you can access the `SonarQube Server` on `http://<ip-address>:9000` 
Sonar server start on port 9000 by default. so open port 9000 in inbound rules under security groups to access sonar UI.
Sonar server default username is admin and password is admin.>> crete your new password and enter.

You can setup Sonar server any where only thing is your AWS should have connectivity to it.<br/>
In organization, setup sonar server in same VPC(else you have to do lot of networking configurations i.e. network Ingress ,VPC pairing etc) with only private IP where your jenkis server and other things are related to your project. 

