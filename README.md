# Corbit

## Запуск LibreOffice в Docker
```shell
docker build -f Dockerfile-libreoffice -t libreoffice-converter .

docker run --rm -v $(pwd):/data libreoffice-converter example.docx
```


## Jodconverter API (Swagger)
http://localhost:9090/swagger-ui.html#/


## Запуск всего приложения
```shell
 docker compose up -d
```


## Где смотреть куда ходить
Если тип аутентификации указан как basic:
http://localhost:9093/

Если тип аутентификации указан как session:
http://localhost:9093/login


## Типы аутентификации
Доступные типы:
- basic
- session
- jwt
- oauth

Файл для смены типа аутентификации расположен:

`/corbit-auth/Dockerfile`


## Если включен тип аутентификации <jwt>

### Запрос на получение токена (делается на сервис аутентификации)
```shell
POST /login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin"
}


curl -v -X POST -H "Content-Type: application/json" -d '{"username":"admin","password":"admin"}' http://localhost:9094/login
```

### Доступ к защищённому ресурсу
```shell
GET /hello
Authorization: Bearer <токен_из_ответа>

curl -v -X GET -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDI4NTI0MywiZXhwIjoxNzYwMjg1ODQzfQ.hYTTMsVT_2zBqNEZREcI-B73mSiqrAWZaRjZ0q0dP08" http://localhost:9094/auth
```


## Добавить
```shell
cat /etc/hosts
##
# Host Database
#
# localhost is used to configure the loopback interface
# when the system is booting.  Do not change this entry.
##
127.0.0.1	localhost
255.255.255.255	broadcasthost
::1             localhost


127.0.0.1 nginx-service
```