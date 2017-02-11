eTasker API v.1.0

eTasker API is RESTFul built on Http

##1.	Register

This endpoint registers new user and send email to new registered user to confirm email.

#### HTTP Request
 
POST https://locallhost:8085/user/api/register

#### Query parameters

| Field | Format | Description |
| :----- |:------| :----------|
| name   | String |~|
|email|String|~|
|companyName|String|~|
|password|String|~|

##2.	Login

This endpoint login user if email confirmed

#### HTTP Request

POST https://locallhost:8085/user/api/login

#### Query parameters

| Field | Format | Description |
| :----- |:------| :----------|
|email|String|~|
|password|String|~|

##3.	Logout

This endpoint logs out user

#### HTTP Request

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
 |code|String|Client code|
 |address|String|Client address|
 |phone|String|Client phone|

* ### Create a new Client

 This endpoint creates a new client.

 #### HTTP Request

 POST https://locallhost:8085/user/api/clients

 If your request will be successfull, you will recieve 201 (Created) status code and newly created client as JSON.

* ### Update an existing Client

 This endpoint updates an existing client.

 #### HTTP Request

 PUT https://locallhost:8085/user/api/clients/{id}

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
 |name|String|Object's name|
 |client|Int|Client ID|

* ### Create a new Object

 This endpoint creates a new object.

 #### HTTP Request

 POST https://locallhost:8085/user/api/objects

 If your request will be successfull, you will recieve 201 (Created) status code and newly created object as JSON.

* ### Update an existing Object

 This endpoint updates an existing object.

 #### HTTP Request

 PUT https://locallhost:8085/user/api/objects/{id}

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

 PUT https://locallhost:8085/user/api/materials/{id}

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
 |password|String| Login password |
 |companyName |String |Company name |
 |created|time stamp|date created|
 |updated|time stamp|date updated|

* ### Create a new Worker

 This endpoint creates a new worker.

 #### HTTP Request

 POST https://locallhost:8085/user/api/workers

 If your request will be successfull, you will recieve 201 (Created) status code and newly created worker as JSON.

* ### Update an existing Worker

 This endpoint updates an existing worker.

 #### HTTP Request

 PUT https://locallhost:8085/user/api/workers/{id}

 If your request will be successfull, you will recieve 204 (No Content) status code.

##8. Tasks

* ### Get all tasks

 This endpoint retrieves all tasks

 #### HTTP Request

 GET https://locallhost:8085/user/api/tasks

 If your request successfull, you will receive 200 (OK) status code and all tasks as JSON.

* ### Get an existing Task

 This endpoint retrieves a specific task

 #### HTTP Request

 GET https://locallhost:8085/user/api/tasks/{id}

 If your request successfull, you will receive 200 (OK) status code and specific task as JSON.
 
 | Field | Format | Description |
 | :----- |:------| :----------|
 | id    | String |Item id|
 |description|String|Task description|
 |rating|int|Rating by client (1 - bad, 2 - average, 3 - good)|
 |agreement|boolean|true if client agreed |
 |signatureType|int|	(0 - no signature, 1 - signature) |
 |abortMessage|String|Abort message|
 |clientNote|String|Note written by client|
 |clientNoteReply|String|Reply to client note by administrator|
 |distance|int|Distance in meters|
 |workPrice|int|Total price of Task works in cents|
 |materialPrice|int|Total price of all Task materials in cents|
 |finalPrice|int|workPrice + materialPrice|
 |status|int|	Current Task status	(0	- Unassigned, 1 - In progress, 2	- Done, 3 -	Aborted)|
 |plannedTime|timestamp|expected task start time|
 |plannedEndTime|timestamp|expected task end time|
 |fetched|boolean|true if Task was fetched by worker device|
 |startTime|timestamp|Task start|
 |endTime|timestamp|Task end|
 |startOnTime|boolean|true if Task started on time|
 |duration|int|Total duration of Task works in minutes|
 |billStatus|boolean|true if client was billed|
 |billDate|timestamp|when client was billed|
 |fileExists|boolean|true if Task has files|
 |worker|String|worker id|
 |taskType|String| task type id|
 |object|String|object id|
 |client|String|client id|
 |created|timestamp|~|
 |updated|timestamp|~|

* ### Create a new Task

 This endpoint creates a new task.

 #### HTTP Request

 POST https://locallhost:8085/user/api/tasks

 If your request will be successfull, you will recieve 201 (Created) status code and newly created task as JSON.

* ### Update an existing Task

 This endpoint updates an existing task.

 #### HTTP Request

 PUT https://locallhost:8085/user/api/tasks/{1}

 If your request will be successfull, you will recieve 204 (No Content) status code.

##9. Report

* ### Get Report

 This endpoint retrieves report

 #### HTTP Request

 GET https://locallhost:8085/user/api/report

 If your request successfull, you will receive 200 (OK) status code and report as JSON.
 
 | Field | Format | Description |
 | :----- |:------| :----------|
 | id    | String |Item id|
 |companyName|String|Company name|
 |companyCode|String|~|
 |companyAddress|String|~|
 |companyPhone|String|~|
 |reportText|String|~|
 |showPrice|boolean|true if show price in report|
 |showDescription|boolean|~|
 |showStart|boolean|true if show task start time|
 |showFinish|boolean|~|
 |showDuration|boolean|~|

* ### Update Report

 This endpoint updates report layout.

 ####HTTP Request

 PUT https://locallhost:8085/user/api/report

 If your request will be successfull, you will recieve 204 (No Content) status code.

## 10. Task Type

* ### Get all task types

 This endpoint retrieves all task types

 #### HTTP Request

 GET https://locallhost:8085/user/api/task_types

 If your request successfull, you will receive 200 (OK) status code and all task types as JSON.

* ### Get an existing Task Type

 This endpoint retrieves a specific task type

 #### HTTP Request

 GET https://locallhost:8085/user/api/task_types/{id}

 If your request successfull, you will receive 200 (OK) status code and specific task type as JSON.

 | Field | Format | Description |
 | :----- |:------| :----------|
 | id    | String |Item id|
 |title|String|Task type title|
 |mailList|String|List of additional summary receivers (seprated by “;”)|
 |signature|boolean|true if signature required|
 |created|timestamp|~|
 |updated|timestamp|~|

* ### Create a new Task Type

 This endpoint creates a new task type.

 #### HTTP Request

 POST https://locallhost:8085/user/api/task_types

 If your request will be successfull, you will recieve 201 (Created) status code and newly created task type as JSON.

* ### Update an existing Task Type

 This endpoint updates an existing task type.

 #### HTTP Request

 PUT https://locallhost:8085/user/api/task_types/{id}

 If your request will be successfull, you will recieve 204 (No Content) status code.

## 11. Responsible Person

* ### Get all responsible persons

 This endpoint retrieves all responsible persons

 #### HTTP Request

 GET https://locallhost:8085/user/api/responsible_persons

 If your request successfull, you will receive 200 (OK) status code and all responsible persons as JSON.

* ### Get an existing Responsible Person

 This endpoint retrieves a specific responsible person

 #### HTTP Request

 GET https://locallhost:8085/user/api/responsible_person/{id}

 If your request successfull, you will receive 200 (OK) status code and specific responsible person as JSON.

 | Field | Format | Description |
 | :----- |:------| :----------|
 | id    | String |Item id|
 |firstName|String|~|
 |lastName|String|~|
 |phone|String|~|
 |email|String|~|
 |client|String|client id|
 |created|timestamp|~|
 |updated|timestamp|~|

* ### Create a new Responsible Person

 This endpoint creates a new responsible person.

 #### HTTP Request

 POST https://locallhost:8085/user/api/responsible_person

 If your request will be successfull, you will recieve 201 (Created) status code and newly created responsible person as JSON.

* ### Update an existing Responsible Person

 This endpoint updates an existing responsible person.

 #### HTTP Request

 PUT https://locallhost:8085/user/api/responsible_person/{id}

 If your request will be successfull, you will recieve 204 (No Content) status code.

## 12. Image

* ### Get all images

 This endpoint retrieves all images.

 #### HTTP Request

 GET https://locallhost:8085/user/api/images

 If your request successfull, you will receive 200 (OK) status code and all images as JSON.

* ### Get an existing Image

 This endpoint retrieves a specific image.

 #### HTTP Request

 GET https://locallhost:8085/user/api/images/{id}

 If your request successfull, you will receive 200 (OK) status code and image.
 
 | Field | Format | Description |
 | :----- |:------| :----------|
 | id    | String |Item id|
 | name|String|image name|
 |path|String|image path on server|
 |task|String|task id|
 |created|timestamp|~|
 
* ### Download image

 This endpoint downloads specific image.

 #### HTTP Request

 GET https://locallhost:8085/user/api/images/{id}/download

 If your request will be successfull, you will recieve 200 (OK) status code and image.

* ### Upload new image

 This endpoint stores new image on server.

 #### HTTP Request

 POST https://locallhost:8085/user/api/images

 If your request will be successfull, you will recieve 200 (OK) status code.
