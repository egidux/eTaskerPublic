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

##4. Clients

###Get all Clients

This endpoint retrieves all clients

####HTTP Request

GET https://locallhost:8085/user/api/clients

If your request successfull, you will receive 200 (OK) status code and all clients as JSON.

###Get an existing Client

This endpoint retrieves a specific client

####HTTP Request

GET https://locallhost:8085/user/api/clients/{id}

If your request successfull, you will receive 200 (OK) status code and specific client as JSON.

###Create a new Client

This endpoint creates a new client.

####HTTP Request

POST https://locallhost:8085/user/api/clients

If your request will be successfull, you will recieve 201 (Created) status code and newly created client as JSON.

###Update an existing Client

This endpoint updates an existing client.

####HTTP Request

PUT https://locallhost:8085/user/api/clients

If your request will be successfull, you will recieve 204 (No Content) status code.

##5. Objects

###Get all objects

This endpoint retrieves all objects

####HTTP Request

GET https://locallhost:8085/user/api/objects

If your request successfull, you will receive 200 (OK) status code and all objects as JSON.

###Get an existing Object

This endpoint retrieves a specific object

####HTTP Request

GET https://locallhost:8085/user/api/objects/{id}

If your request successfull, you will receive 200 (OK) status code and specific object as JSON.

###Create a new Object

This endpoint creates a new object.

####HTTP Request

POST https://locallhost:8085/user/api/objects

If your request will be successfull, you will recieve 201 (Created) status code and newly created object as JSON.

###Update an existing Object

This endpoint updates an existing object.

####HTTP Request

PUT https://locallhost:8085/user/api/objects

If your request will be successfull, you will recieve 204 (No Content) status code.

##6. Materials

###Get all materials

This endpoint retrieves all materials

####HTTP Request

GET https://locallhost:8085/user/api/materials

If your request successfull, you will receive 200 (OK) status code and all materials as JSON.

###Get an existing Material

This endpoint retrieves a specific material

####HTTP Request

GET https://locallhost:8085/user/api/materials/{id}

If your request successfull, you will receive 200 (OK) status code and specific material as JSON.

###Create a new Material

This endpoint creates a new material.

####HTTP Request

POST https://locallhost:8085/user/api/materials

If your request will be successfull, you will recieve 201 (Created) status code and newly created material as JSON.

###Update an existing Material

This endpoint updates an existing material.

####HTTP Request

PUT https://locallhost:8085/user/api/materials

If your request will be successfull, you will recieve 204 (No Content) status code.



