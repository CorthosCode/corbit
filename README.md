# Corbit

---

![corbit](/resources/corbit-app.png)

Песочница в виде PDF-конвертера. Все сервисы, составляющие приложение, завернуты в lightweight-docker-образы. Конвертируются все популярные расширения в PDF (без обратного функционала).

---

## Использование

Запуск приложения в Docker (требуется предварительный запуск docker-daemon).

```shell
 docker compose up -d
```

Запуск приложения с замером скорости запуска (по каждому сервису).

```bash
sh ./start-full.sh
```

```PowerShell
PowerShell -file start-full.ps1
```

---

## Перед запуском приложения

Перед запуском приложения по скрипту необходимо добавить в:
- `/etc/host`
- `C:\Windows\System32\drivers\etc\hosts`

```bash
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

---

## Запуск

1. Создаем пользователя (админка): http://nginx-service:9095
2. Логинимся и пользуемся: http://nginx-service:9093

---

## Техинфо

Техническаяы информация по важным моментам в сервисах.

### Health-check у Keycloak-service

В docker-compose  присутствует проверка:

```bash
  keycloak-service:
    build:
      context: ./keycloak-service/
      dockerfile: Dockerfile
    healthcheck:
      test: [ "CMD-SHELL", "exec 3<>/dev/tcp/localhost/9000; echo -e 'GET /health/ready HTTP/1.1\\r\\nHost: localhost\\r\\nConnection: close\\r\\n\\r\\n' >&3; head -n 1 <&3 | grep -q '200 OK'" ]
      interval: 10s
      timeout: 5s
      retries: 5
```

Детальный разбор:

`exec 3<>/dev/tcp/localhost/9000`

> bash-специфичный способ открыть TCP-соединение с локальным хостом на порт 9000 (где работает Keycloak). Файловый дескриптор 3 открывается для чтения и записи.

`echo -e 'GET /health/ready HTTP/1.1\r\nHost: localhost\r\nConnection: close\r\n\r\n' >&3`

> Отправляет HTTP-запрос типа GET на путь `/health/ready` через открытое соединение (дескриптор 3). Символы `\r\n` - обязательные для HTTP-запроса переносы строки.

`head -n 1 <&3`

> читает первую строку ответа сервера (обычно это статус HTTP ответа, например, "HTTP/1.1 200 OK").

`grep -q '200 OK'`

> проверяет, содержит ли эта строка подстроку "200 OK", что означает успешный ответ HTTP.


### Запуск LibreOffice в Docker

```bash
docker build -f Dockerfile-libreoffice -t libreoffice-converter .

docker run --rm -v $(pwd):/data libreoffice-converter example.docx
```


### Jodconverter API (Swagger)

http://localhost:9090/swagger-ui.html#/


### Пример логов запуска приложения через скрипт 

```
PS C:\Users\USER\IdeaProjects\corbit> PowerShell -file services-speed-test.ps1
[+] Running 16/16
✔ converter-service Pulled                                                                                                                                                                                                  43.1s
✔ 4f4fb700ef54 Pull complete                                                                                                                                                                                               0.1s
✔ fb60efab8139 Pull complete                                                                                                                                                                                               7.8s
✔ ab3308a6b666 Pull complete                                                                                                                                                                                              10.0s
✔ ed7f0d0e02ce Pull complete                                                                                                                                                                                               0.9s
✔ f6e636f1db4f Pull complete                                                                                                                                                                                              16.9s
✔ 64e4860d3f49 Pull complete                                                                                                                                                                                              32.6s
✔ 8c59bc613e90 Pull complete                                                                                                                                                                                              32.7s
✔ e92dc7d3abd9 Pull complete                                                                                                                                                                                              36.9s
✔ 8c0426bbc8a7 Pull complete                                                                                                                                                                                              39.9s
✔ bb7f253d0076 Pull complete                                                                                                                                                                                              40.1s
✔ 1b47fd5d91e1 Pull complete                                                                                                                                                                                               0.9s
✔ ae5fc01b2bb8 Pull complete                                                                                                                                                                                               0.9s
✔ 59214a28920c Pull complete                                                                                                                                                                                               1.8s
✔ 11ec8790e50a Pull complete                                                                                                                                                                                              40.3s
✔ 5ddd3721a2ac Pull complete                                                                                                                                                                                              40.4s
[+] Building 3.3s (53/53) FINISHED
[+] Running 12/12
✔ corbit-backend-service               Built                                                                                                                                                                                 0.0s
✔ corbit-frontend-service              Built                                                                                                                                                                                 0.0s
✔ corbit-nginx-service                 Built                                                                                                                                                                                 0.0s
✔ corbit-keycloak-service              Built                                                                                                                                                                                 0.0s
✔ corbit-auth-service                  Built                                                                                                                                                                                 0.0s
✔ Network corbit_default               Created                                                                                                                                                                               0.1s
✔ Container corbit-keycloak-service-1  Healthy                                                                                                                                                                              52.3s
✔ Container converter-service          Started                                                                                                                                                                               1.3s
✔ Container corbit-auth-service-1      Started                                                                                                                                                                              52.4s
✔ Container corbit-backend-service-1   Started                                                                                                                                                                               1.6s
✔ Container corbit-frontend-service-1  Started                                                                                                                                                                               2.1s
✔ Container corbit-nginx-service-1     Started                                                                                                                                                                              52.6s
Service startup time (ms):
nginx-service : 101128 (ms) -> 101.128 (sec)
backend-service : 100291 (ms) -> 100.291 (sec)
keycloak-service : 100635 (ms) -> 100.635 (sec)
converter-service : 100133 (ms) -> 100.133 (sec)
auth-service : 100844 (ms) -> 100.844 (sec)
frontend-service : 100461 (ms) -> 100.461 (sec)
------------------------------
Total: 101128 (ms) -> 101.128 (sec)


PS C:\Users\USER\IdeaProjects\corbit> PowerShell -file services-speed-test.ps1
[+] Running 7/7
✔ Container corbit-nginx-service-1     Removed                                                                                                                                                                               0.3s
✔ Container corbit-auth-service-1      Removed                                                                                                                                                                               0.4s
✔ Container corbit-frontend-service-1  Removed                                                                                                                                                                               0.3s
✔ Container corbit-backend-service-1   Removed                                                                                                                                                                               1.2s
✔ Container corbit-keycloak-service-1  Removed                                                                                                                                                                               1.4s
✔ Container converter-service          Removed                                                                                                                                                                               0.4s
✔ Network corbit_default               Removed                                                                                                                                                                               0.2s
[+] Running 7/7
✔ Network corbit_default               Created                                                                                                                                                                               0.0s
✔ Container converter-service          Started                                                                                                                                                                               1.5s
✔ Container corbit-keycloak-service-1  Healthy                                                                                                                                                                              53.0s
✔ Container corbit-auth-service-1      Started                                                                                                                                                                              53.0s
✔ Container corbit-backend-service-1   Started                                                                                                                                                                               1.9s
✔ Container corbit-frontend-service-1  Started                                                                                                                                                                               2.4s
✔ Container corbit-nginx-service-1     Started                                                                                                                                                                              53.1s
Service startup time (ms):
nginx-service : 55170 (ms) -> 55.17 (sec)
backend-service : 54750 (ms) -> 54.75 (sec)
keycloak-service : 54267 (ms) -> 54.267 (sec)
converter-service : 54593 (ms) -> 54.593 (sec)
auth-service : 54431 (ms) -> 54.431 (sec)
frontend-service : 55025 (ms) -> 55.025 (sec)
------------------------------
Total: 55170 (ms) -> 55.17 (sec)
```