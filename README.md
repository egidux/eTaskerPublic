# eTasker
eTasker is a flexible and effective tool for managing tasks, processes and employees.

Requirements:

Java 8

Maven -> https://maven.apache.org/download.cgi

Redis, install on localhost and run it with the default port (6379) -> http://redis.io/download

First run might run longer

Run tests:

mvn test

Start server (default port 8085, to change port src/main/resources/config/application.properties):

mvn spring-boot:run

https://localhost:8085/user/api
