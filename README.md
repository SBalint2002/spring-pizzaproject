# PizzaProject-spring
Http Requestek:

<accessToken> helyére a tényleges accessToken amit a login-ból kapunk meg
<refreshToken> helyére a tényleges refreshTokent

# --------------------------------USER-----------------------------------------
###
POST http://localhost:8080/user/register
Content-Type: application/json

{
  "first_name": "Elek",
  "last_name": "Teszt",
  "email": "tesztelek@gmail.com",
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
PUT http://localhost:8080/user/3
Content-Type: application/json
Authorization: Bearer <accessToken>

{
  "first_name": "Sárándi",
  "password": "Adminadmin1"
}

###
GET http://localhost:8080/user/data
Content-Type: application/json
Authorization: Bearer <accessToken>

###
POST http://localhost:8080/user/refresh
Content-Type: application/json

{
  "refreshToken": "<refreshToken>"
}

###
GET http://localhost:8080/user/get-all
Content-Type: application/json

###
DELETE http://localhost:8080/user/6
Authorization: Bearer <accessToken>

# --------------------------------PIZZA-----------------------------------------
###
GET http://localhost:8080/pizza/get-all

###
POST http://localhost:8080/pizza/add-pizza
Content-Type: application/json
Authorization: Bearer <accessToken>

{
  "name": "Sonkás",
  "price": "1765",
  "description": "Asd",
  "picture": "xd"
}

###
PUT http://localhost:8080/pizza/5
Content-Type: application/json
Authorization: Bearer <accessToken>

{
  "name": "Sonkássss",
  "price": "1765",
  "description": "Asd",
  "picture": "xd"
}

###
DELETE http://localhost:8080/pizza/5
Authorization: Bearer <accessToken>

# --------------------------------ORDER-----------------------------------------

###
POST http://localhost:8080/order/add-order
Content-Type: application/json
Authorization: Bearer <accessToken>

{
  "location": "Ide",
  "pizzaIds": [ 1, 4 ],
  "phoneNumber": "43-4-324"
}

###
GET http://localhost:8080/order/get-all

###
PUT http://localhost:8080/order/2
Content-Type: application/json
Authorization: Bearer <accessToken>

{
  "ready": "true"
}
