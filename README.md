# Identity Service

This microservice is responsible for:

- Onboarding users
- Roles and permissions
- Authentication

## Tech Stack

- **Build tool**: Maven >= 3.9.5
- **Java**: 17
- **Framework**: Spring Boot 3.3.x
- **DBMS**: MySQL

## Prerequisites

- Java SDK 17
- A MySQL server

## Start Application

```sh
mvn spring-boot:run
```

## Build Application

```sh
mvn clean package
```

## Docker Guideline

### Build Docker Image

```sh
docker build -t <account>/identity-service:1.0.0 .
```

### Push Docker Image to Docker Hub

```sh
docker image push <account>/identity-service:1.0.0
```

### Create Network

```sh
docker network create [your-network]
```

### Start MySQL in [your-network]

```sh
docker run --network [your-network] \  
  --name mysql \  
  -p 3306:3306 \  
  -e MYSQL_ROOT_PASSWORD=root \  
  -d mysql:8.0.36-debian
```

### Run Your Application in [your-network]

```sh
docker run --name identity-service \  
  --network [your-network] \  
  -p 8080:8080 \  
  -e DBMS_CONNECTION=jdbc:mysql://mysql:3306/identity_service \  
  identity-service:1.0.0
```

---

**Source**: [Devteria](https://www.youtube.com/playlist?list=PL2xsxmVse9IaxzE8Mght4CFltGOqcG6FC)

**Front-end Repo**: [Front-end](https://github.com/shongon-be/identity-frontend/tree/main)
