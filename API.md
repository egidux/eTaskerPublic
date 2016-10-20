eTasker API v.1.0

eTasker API is RESTFul built on Http

All http requests return response code. When error occured response will contain JSON describing error e.g {"error", "please login"}

Http status codes:

200 OK

201 Created

409 Conflict

400 Bad Request

500 Internal Server Error

401 Unauthorized

###1.	Register

This endpoint registers new user

POST https://locallhost:8085/user/api/register

Query parameters:


name (required), email (required), company name (required), password (required)

###2.	Login

This endpoint login user when email confirmed

POST https://locallhost:8085/user/api/login

Query parameters:

email (required), password (required)

###3.	Logout

This endpoint logs out user

GET https://locallhost:8085/user/api/logout


