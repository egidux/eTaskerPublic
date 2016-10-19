eTasker API v.1.0

eTasker API is RESTFul
Api returns Http status code 200 or error code with JSON describing error
1.	Register
This endpoint registers new user
POST https://locallhost:8085/user/api/register
Query parameters:
name (required), email (required), company name (required), password (required)
2.	Login
This endpoint login user when email confirmed
POST https://locallhost:8085/user/api/login
Query parameters:
email (required), password (required)
3.	Logout
This endpoint logs out user
GET https://locallhost:8085/user/api/logout


