# Markov Chain Algorithm  (v1.0.0)

Markov Chain Algorithm for natural language text transformation.

  - **markov-chain-lib** - Provides ``MarkovChainBuilder`` class for configuring new instances of ``MarkovChain`` from a source natural language text file according to some different possible settings. With a configured ``MarkovChain`` instance in hands new pseud-random text can be generated on demand.
  - **markov-web-tool** - Uses [Spring Boot](http://projects.spring.io/spring-boot/) to create an uber jar providing a standalone web-application that used the above Markov Chain Library from the aforementioned module. The front-end is a simple single-paged [AngularJS](https://angularjs.org/) application styled with straight [Booststrap CSS](http://getbootstrap.com/css/) that talks to the back-end through a Restful API built uppon Spring Framework stack.

### Requires

 - Java 8 JDK
 - Maven 3

### Building Project and Serving the Web App
In the project root (``markov-parent``) run maven install, what will also build both modules.
 ```sh
$ mvn clean install
```
After a Successful build, you are ready to goa with the ``markov-web-tool`` uber jar, just run it and spring-boot will take care of serving the web service in port ``8080``.
```sh
java -jar markov-web-tool/target/markov-web-tool-1.0.0.jar
```
After a few seconds the web application will be available in ``http://localhost:8080``. 

Enjoy!
