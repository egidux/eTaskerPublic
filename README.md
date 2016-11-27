# eTasker
eTasker is a flexible and effective tool for managing tasks, processes and employees.

###Requirements:

Java 8

Maven -> https://maven.apache.org/download.cgi

Redis (for http session management), install on localhost and run it with the default port (6379) -> http://redis.io/download

First run might be longer

###Run tests:

mvn test

###Start server (to change default port src/main/resources/config/application.properties):

mvn spring-boot:run

[https://localhost:8085](https://localhost:8085)

###Test API

Install Postman from google Chrome Web Store

Import test collection into Postman from Link: https://www.getpostman.com/collections/fca07e5986b51fb1dc6d or File

Entity endpoints in test collection has methods: GET(retrieves all entries as JSON and retuns Http status OK), POST(creates new entry and returns entry as JSON and Http status 201), GET https://.../1 (retrieves specific entry as JSON and Http status OK), PUT https://.../1 (updates specific entry and returns Http status 204)

