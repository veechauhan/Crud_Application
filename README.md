# PlayFramework-CRUD-APIs

-> curl localhost:9000/users
[]

-> curl localhost:9000/users -H 'Content-Type: application/json' -d '{"name":"user","password":"developer" }' 
{ "id": "1b069c7b-225e-4ea7-a9ee-07992f69a8a2","name": "user","password": "developer"}

-> curl localhost:9000/users
{ "id": "1b069c7b-225e-4ea7-a9ee-07992f69a8a2","name": "user","password": "developer"}

-> curl localhost:9000/users/1b069c7b-225e-4ea7-a9ee-07992f69a8a2 -X DELETE

-> curl localhost:9000/users
[]
