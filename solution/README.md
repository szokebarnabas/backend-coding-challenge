README
====

Requirements: Docker, Node, Gulp

Start backend:
```
./run_backend.sh
```

Start frontend:
```
npm install -g gulp && npm install && gulp
```

Endpoints
--------------

Save expense:
```
POST {host}:{port}/v1/expense

{
  "date": "29/02/2018",
  "amount": "333.32",
  "reason": "banana"
}
```


Find expense by id:
```
GET {host}:{port}/v1/expense/{id}
```

Find all expenses:
```
GET {host}:{port}/v1/expense/
```
