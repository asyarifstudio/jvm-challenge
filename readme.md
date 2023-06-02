# Introduction
This microservice is to answer the challenge. It is built using spring with below requirement
- openjdk version 11
- mvn version 3.6.3
- Docker version 23.0.2, build 569dd73

other requirements are listed in the [root pom.xml](pom.xml), project pom.xml, and [docker-compose.yml](docker-compose.yml)

## Architecture

The general architecture of the microservice can be seen in the figure below

```mermaid
C4Context   
    title System Diagram for MicroService
    Person(clientA,"Client")

    Enterprise_Boundary(b0,"Entry Point"){
        Container(apiGateway,"API Gateway")
        Container(keyCloack,"KeyCloak")
    }

    

    Enterprise_Boundary(b1,"Microservice"){
        Container(discoveryService,"Discovery Server")
        Container(productService,"Product Service")
        Container(reviewService,"Review Service")
        Container(redis,"Redis")
        Container(mongo,"MongoDB")
    }

    Rel(clientA,apiGateway,"request")
    Rel(clientA,keyCloack,"authenticate")
    Rel(apiGateway,discoveryService,"register & forward request")
    BiRel(productService,discoveryService,"register & forward request")
    BiRel(reviewService,discoveryService,"register & forward request")
    Rel(apiGateway,keyCloack,"Authorize")
    Rel(productService,reviewService,"getReviews")
    Rel(reviewService,redis,"cache")
    Rel(redis,mongo,"fetch")
    UpdateLayoutConfig($c4ShapeInRow="2", $c4BoundaryInRow="1")
```
# Compilation
To compile, simply run `mvn package` from the root directory

# Deployment
before deploying, please modify the host DNS in your machine to allow keycloak iss resolution. 
- Windows : add `127.0.0.1 keycloak` to C:\Windows\System32\drivers\etc\hosts
- Linux/Mac : add `127.0.0.1 keycloak` to /etc/host
after that, you can deploy the microservice using command `docker compose up -d`
the gateway is available in http://localhost:8181

