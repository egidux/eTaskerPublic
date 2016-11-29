# eTasker
eTasker is a flexible and effective tool for managing tasks, processes and employees.

###Requirements:

Java 8

Maven -> https://maven.apache.org/download.cgi

Redis (for http session management), install on localhost and run it with the default port (6379) -> http://redis.io/download

###Run tests (partly done):
```
mvn test
```
###Start server (to change default port src/main/resources/config/application.properties):
```
mvn spring-boot:run
```
###Test API

Install Postman from google Chrome Web Store -> https://chrome.google.com/webstore/detail/postman/fhbjgbiflinjbdggehcddcbncdddomop?hl=en

Import test collection into Postman from Link: https://www.getpostman.com/collections/fca07e5986b51fb1dc6d or File

Postman might get error connecting, need to accept unsecure SSL connection -> open in google chrome https://localhost:8085/user/api/users -> ADVANCED -> Proceed to localhost (unsafe)

all API information in API.md 

To edit request parameter click Params in Postman, to upload image click Body->form-data->Choose Files

Entity endpoints in test collection has methods: GET(retrieves all entries as JSON and retuns Http status OK), POST(creates new entry and returns entry as JSON and Http status 201), GET https://.../{id} (retrieves specific entry as JSON and Http status OK), PUT https://.../{id} (updates specific entry and returns Http status 204)
