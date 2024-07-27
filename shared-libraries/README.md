# Shared Libraries

In Jenkins, a shared library is a way to store commonly used code(reusable code), such as scripts or functions, that can be used by different 
Jenkins pipelines. 

Instead of writing the same code again and again in multiple pipelines, you can create a shared library and use it in all the pipelines
that need it. This can make your code more organized and easier to maintain. 

Think of it like a library of books, Instead of buying the same book over and over again, you can borrow it from the library whenever you need it.

## Advantages

- Standarization of Pipelines
- Reduce duplication of code
- Easy onboarding of new applications, projects or teams
- One place to fix issues with the shared or common code
- Code Maintainence 
- Reduce the risk of errors

![Screenshot 2023-05-02 at 9 47 24 PM](https://user-images.githubusercontent.com/43399466/235724851-90a5cad6-ac0d-428b-9944-93fffea55180.png)


### Jenkins Shared Libraries: Comprehensive Learning Notes

#### Introduction
Jenkins Shared Libraries offer a powerful way to manage and reuse common code across multiple Jenkins pipelines. This technique is particularly useful in large organizations where numerous microservices are managed, each requiring its own CI/CD pipeline. Shared libraries enable the standardization of pipelines, reduce duplication, and simplify maintenance.

#### Concept of Shared Libraries
A Shared Library in Jenkins is a repository of common code that can be used across multiple pipelines. The primary purpose is to avoid repeating the same code in each pipeline, which can lead to maintenance challenges and inconsistencies.

**Key Components:**
1. **Global Variables and Methods:** Shared code that can be called within Jenkins pipelines.
2. **Reusable Functions:** Encapsulated logic that can be invoked with parameters.
3. **Standardization:** Ensures that all pipelines follow a consistent structure and practice.

#### Advantages of Using Shared Libraries
1. **Reduced Code Duplication:** Common tasks such as checking out code, running tests, or deploying to production are centralized in one location.
2. **Easier Maintenance:** Changes in one place can be propagated across all pipelines, making it easier to update or fix issues.
3. **Consistent Quality and Security Practices:** Ensures that all pipelines adhere to the same quality and security standards.
4. **Improved Onboarding:** New team members can easily understand and use existing pipelines, reducing the learning curve.
5. **Low Code Approach:** By abstracting common tasks, the amount of code written in individual pipelines is reduced, leading to fewer errors and a more streamlined development process.

#### Implementing Shared Libraries

**Structure:**
1. **`vars` Folder:** Contains Groovy files that define the reusable functions. Each file represents a global variable in the pipeline scripts.
2. **`src` Folder:** Used for more complex classes and functions, which can be organized into packages.
3. **`resources` Folder:** Contains additional resources, such as scripts or configuration files, that the shared library might need.

jenkins-shared-library/
│
├── src/
│   └── org/
│       └── example/
│           └── MyLibrary.groovy
├── (root of repository)
├── vars/
│   ├── sonarQubeScan.groovy
│   ├── trivyScan.groovy
│   ├── dockerBuild.groovy
│   ├── dockerPush.groovy
├── README.md

├── resources/
│   └── someResource.txt
└── README.md


**Basic Example:**

To define a shared library, you would create a Groovy file in the `vars` folder, for example, `helloWorld.groovy`:

```groovy
def call() {
    echo 'Hello from the shared library!'
}
```

In your Jenkins pipeline, you can use this shared library as follows:

```groovy
@Library('my-shared-library') _
helloWorld()
```

Here, `@Library('my-shared-library')` imports the shared library named "my-shared-library," and the function `helloWorld()` is called from that library.

#### Best Practices
1. **Version Control:** Always version your shared libraries to manage changes and ensure stability across different environments.
2. **Documentation:** Clearly document the purpose and usage of each function in the shared library to aid understanding and future maintenance.
3. **Testing:** Thoroughly test shared library functions independently to avoid introducing issues across multiple pipelines.
4. **Modular Design:** Keep functions small and focused on a single task to promote reuse and maintainability.
5. **Security:** Ensure that sensitive data and credentials are handled securely within shared libraries, avoiding exposure in logs or source control.

#### Advanced Usage
For more complex requirements, shared libraries can include:
- **Parameterized Functions:** Allowing customization for different pipeline needs.
- **Helper Classes and Methods:** Encapsulating more complex logic in the `src` folder.
- **Resource Management:** Using the `resources` folder for scripts or configuration files that need to be packaged with the library.

#### Example Use Cases
- **Common Build and Deploy Steps:** For instance, checking out code, running unit tests, building artifacts, and deploying them.
- **Environment-Specific Configurations:** Managing different configurations for development, staging, and production environments.
- **Security and Compliance Checks:** Centralizing security checks to ensure compliance with organizational policies.<br/>

example- if you have to update any configuration then you have to go to 100s of pipeline to make changes which is not feasible. whereas if you are using shared libraries concept you can make changes at one central location and it will reflect in all jenkins pipeline in your organization.

#### Conclusion
Jenkins Shared Libraries provide a robust solution for managing common code and processes across multiple Jenkins pipelines. They help streamline CI/CD practices, reduce maintenance efforts, and ensure consistency across an organization. By implementing shared libraries, teams can focus on building new features rather than duplicating code and configurations.