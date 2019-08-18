# distributed-signup

This application is a [solution](Distributed_Signup.pdf).

This application includes two maven modules:

 - [persistence-microservice](persistence-microservice)  
 - [signup-microservice](signup-microservice)
 
 For more information, refer to the **[persistence-microservice](persistence-microservice/README.md)**,  **[signup-microservice](signup-microservice/README.md)**
## Build and Run

###  Prerequisites

You have to install [Docker](https://docs.docker.com/).

To build this project use:
  
     mvn clean install
  

### Using Docker to simplify development

To managing Docker images and containers used [docker-maven-plugin](https://github.com/fabric8io/docker-maven-plugin).
To run full this project (must do it after build) with Maven use:

step 1

    cd signup-microservice
    
step 2
    
    mvn docker:run
    
step 3

    cd ../persistence-microservice/

step 4
 
    mvn docker:run 

    
## Test the example:
After fully run application, in a new shell:

    curl -X POST "http://0.0.0.0:8080/api/signup" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"email\": \"test@test.com\", \"password\": \"123456\"}"

Then can check the result in the mongo database.
Signup data must be in **'persistence'** schema, collection is **'profile'**.