# Corbit - PDF-конвертер

Современное React-приложение для конвертации файлов в PDF.

## Установка зависимостей
```bash
npm install
```

## Запуск в режиме разработки
```bash
npm run dev
```
Приложение будет доступно по адресу: http://localhost:5173

## Превью собранного приложения
```bash
npm run preview
```

## Очистка кэша и переустановка
Если возникают проблемы с зависимостями или сборкой:

```bash
# Удаляем node_modules и package-lock.json
rm -rf node_modules package-lock.json

# Очищаем npm кэш
npm cache clean --force

# Переустанавливаем зависимости
npm install
```