# Corbit

## Запуск LibreOffice в Docker
```shell
docker build -t libreoffice-converter .

docker run --rm -v $(pwd):/data libreoffice-converter example.docx
```

## Запуск LibreOffice + java-обертка с пулами
```shell
 docker compose up -d
 
# Докумментация по API
 http://localhost:9090/swagger-ui.html#/
```

## Улучшения
- [x] Разделить сервисы по каталогам
- [x] При обрыве соединения от фронта, созданный временный файл не удаляется