# Http Requestek:

accessToken helyére a tényleges accessToken amit a login-ból kapunk meg  
refreshToken helyére a tényleges refreshTokent

# USER -------------------------------
###
POST http://localhost:8080/user/register  
Content-Type: application/json  
  
{  
"first_name": "Elek5",   
"last_name": "Teszt",  
"email": "tesztelek5@gmail.com",  
"password": "Adminadmin1"  
}

###
POST http://localhost:8080/user/login  
Content-Type: application/json

{  
"email": "tesztelek@gmail.com",  
"password": "Adminadmin1"  
}

###
POST http://localhost:8080/user/admin-login
Content-Type: application/json

{
  "email": "tesztelek@gmail.com",
  "password": "Adminadmin1"
}

###
POST http://localhost:8080/user/refresh  
Content-Type: application/json   

{  
"refreshToken": "refreshToken"
}

###
GET http://localhost:8080/user/data  
Content-Type: application/json  
Authorization: Bearer accessToken

###
PUT http://localhost:8080/user/7  
Content-Type: application/json  
Authorization: Bearer accessToken

{  
"first_name": "Elek",  
"password": "Adminadmin1"  
}

###
GET http://localhost:8080/user/get-all  
Content-Type: application/json  
Authorization: Bearer accessToken

###
DELETE http://localhost:8080/user/6
Authorization: Bearer accessToken

###
# PIZZA --------------------------------

GET http://localhost:8080/pizza/get-all

###
POST http://localhost:8080/pizza/add-pizza   
Content-Type: application/json  
Authorization: Bearer accessToken

{  
"name": "Sajtos",  
"price": "2100",  
"description": "fincsmincsa",  
"picture": "{képlink}",  
"available": true  
}

###
PUT http://localhost:8080/pizza/6  
Content-Type: application/json  
Authorization: Bearer accessToken

{  
"name": "Sonkás",  
"price": "1765",  
"description": "Jó",  
"picture": "{képlink}"  
}

###
# ORDER --------------------------------

###
POST http://localhost:8080/order/add-order  
Content-Type: application/json  
Authorization: Bearer accessToken

{  
"location": "Pokol",  
"pizzaIds": [ 1, 3, 3 ],  
"phoneNumber": "0630-345-4375"  
}

###
GET http://localhost:8080/order/get-all  
Authorization: Bearer accessToken

###
PUT http://localhost:8080/order/1  
Content-Type: application/json  
Authorization: Bearer accessToken

{  
"ready": "true"  
}
