eTasker API v.1.0

eTasker API is RESTFul built on Http

##1.	Register

This endpoint registers new user

POST https://locallhost:8085/user/api/register

Query parameters:


name (required), email (required), company name (required), password (required)

##2.	Login

This endpoint login user when email confirmed

POST https://locallhost:8085/user/api/login

Query parameters:

email (required), password (required)

##3.	Logout

This endpoint logs out user

GET https://locallhost:8085/user/api/logout

## 4. Clients

* ### Get all Clients

 This endpoint retrieves all clients

 #### HTTP Request

 GET https://locallhost:8085/user/api/clients

 If your request successfull, you will receive 200 (OK) status code and all clients as JSON.

* ### Get an existing Client

 This endpoint retrieves a specific client

 #### HTTP Request

 GET https://locallhost:8085/user/api/clients/{id}

 If your request successfull, you will receive 200 (OK) status code and specific client as JSON.

 | Field | Format | Description |
 | :----- |:------| :----------|
 | id    | String |Item id|
 |email|String|Client email|
 |name|String|Client name|
 |companyName|String|Company name|

* ### Create a new Client

 This endpoint creates a new client.

 #### HTTP Request

 POST https://locallhost:8085/user/api/clients

 If your request will be successfull, you will recieve 201 (Created) status code and newly created client as JSON.

* ### Update an existing Client

 This endpoint updates an existing client.

 #### HTTP Request

 PUT https://locallhost:8085/user/api/clients{id}

 If your request will be successfull, you will recieve 204 (No Content) status code.

## 5. Objects

* ### Get all objects

 This endpoint retrieves all objects

 #### HTTP Request

 GET https://locallhost:8085/user/api/objects

 If your request successfull, you will receive 200 (OK) status code and all objects as JSON.

* ### Get an existing Object

 This endpoint retrieves a specific object

 #### HTTP Request

 GET https://locallhost:8085/user/api/objects/{id}

 If your request successfull, you will receive 200 (OK) status code and specific object as JSON.

 | Field | Format | Description |
 | :----- |:------| :----------|
 | id    | String |Item id|
 |address|String|Object address|
 |responsiblePerson|String|Responsible person id|
 |created|timestamp|~|
 |updated|timestamp|~|

* ### Create a new Object

 This endpoint creates a new object.

 #### HTTP Request

 POST https://locallhost:8085/user/api/objects

 If your request will be successfull, you will recieve 201 (Created) status code and newly created object as JSON.

* ### Update an existing Object

 This endpoint updates an existing object.

 #### HTTP Request

 PUT https://locallhost:8085/user/api/objects{id}

 If your request will be successfull, you will recieve 204 (No Content) status code.

##6. Materials

* ### Get all materials

 This endpoint retrieves all materials

 #### HTTP Request

 GET https://locallhost:8085/user/api/materials

 If your request successfull, you will receive 200 (OK) status code and all materials as JSON.

* ### Get an existing Material

 This endpoint retrieves a specific material

 #### HTTP Request

 GET https://locallhost:8085/user/api/materials/{id}

 If your request successfull, you will receive 200 (OK) status code and specific material as JSON.
 
  | Field | Format | Description |
 | :----- |:------| :----------|
 | id    | String |Item id|
 |title|String|Material title|
 |serial_nbumber|String|~|
 |created|timestamp|~|
 |updated|timestamp|~|

* ### Create a new Material

 This endpoint creates a new material.

 #### HTTP Request

 POST https://locallhost:8085/user/api/materials

 If your request will be successfull, you will recieve 201 (Created) status code and newly created material as JSON.

* ###Update an existing Material

 This endpoint updates an existing material.

 #### HTTP Request

 PUT https://locallhost:8085/user/api/materials{id}

 If your request will be successfull, you will recieve 204 (No Content) status code.

##7. Workers

* ### Get all workers

 This endpoint retrieves all workers

 #### HTTP Request

 GET https://locallhost:8085/user/api/workers

 If your request successfull, you will receive 200 (OK) status code and all workers as JSON.

* ### Get an existing Worker

 This endpoint retrieves a specific worker

 #### HTTP Request

 GET https://locallhost:8085/user/api/workers/{id}

 If your request successfull, you will receive 200 (OK) status code and specific worker as JSON.
 
   | Field | Format | Description |
 | :----- |:------| :----------|
 | id    | String |Item id|
 |email|String|Worker email|
 |name|String|Worker name|
 |password|String|Login password|
 |companyName|String|Company name|

* ### Create a new Worker

 This endpoint creates a new worker.

 #### HTTP Request

 POST https://locallhost:8085/user/api/workers

 If your request will be successfull, you will recieve 201 (Created) status code and newly created worker as JSON.

* ### Update an existing Worker

 This endpoint updates an existing worker.

 #### HTTP Request

 PUT https://locallhost:8085/user/api/workers{id}

 If your request will be successfull, you will recieve 204 (No Content) status code.

##8. Tasks

###Get all tasks

This endpoint retrieves all tasks

####HTTP Request

GET https://locallhost:8085/user/api/tasks

If your request successfull, you will receive 200 (OK) status code and all tasks as JSON.

###Get an existing Task

This endpoint retrieves a specific task

####HTTP Request

GET https://locallhost:8085/user/api/tasks/{id}

If your request successfull, you will receive 200 (OK) status code and specific task as JSON.

###Create a new Task

This endpoint creates a new task.

####HTTP Request

POST https://locallhost:8085/user/api/tasks

If your request will be successfull, you will recieve 201 (Created) status code and newly created task as JSON.

###Update an existing Task

This endpoint updates an existing task.

####HTTP Request

PUT https://locallhost:8085/user/api/tasks

If your request will be successfull, you will recieve 204 (No Content) status code.

##9. Report

###Get Report

This endpoint retrieves report

####HTTP Request

GET https://locallhost:8085/user/api/report

If your request successfull, you will receive 200 (OK) status code and report as JSON.

###Update Report

This endpoint updates report layout.

####HTTP Request

PUT https://locallhost:8085/user/api/report

If your request will be successfull, you will recieve 204 (No Content) status code.

