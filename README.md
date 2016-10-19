# eTasker
eTasker is a flexible and effective tool for managing tasks, processes and employees.

in API doc only what is implemented

eTasker server and Jenkins(continuous integration) will run on VM, around second semester

###Requirements:

Java 8

Maven -> https://maven.apache.org/download.cgi

Redis (for http session management), install on localhost and run it with the default port (6379) -> http://redis.io/download

First run might be longer

###Run tests:

mvn test

###Start server (to change default port src/main/resources/config/application.properties):

mvn spring-boot:run

[https://localhost:8085/user/api](https://localhost:8085/user/api)
