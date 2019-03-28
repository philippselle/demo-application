# DevOps CoC Spring Boot Docker Showcase Application

A simple Spring Boot application running native with mvn or as a Docker container.


## Usage

- Directly using maven
```
mvn spring-boot:run
```

- As a Docker container
```
docker build -t showcase .
docker run docker run --name showcase-demo -d -p 9191:9191 showcase
```
