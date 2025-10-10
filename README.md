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
http://localhost:9094/

Если тип аутентификации указан как session:
http://localhost:9094/login