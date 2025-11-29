#!/bin/bash

# Удаляем все контейнеры для "холодного" старта
docker-compose down

# Получаем список сервисов из docker-compose.yml
services=$(docker-compose config --services)

declare -A start_times

# Запускаем общий таймер
start_all=$(date +%s%3N)

# Запускаем все сервисы в фоне без зависимостей
docker-compose up -d

# Цикл по всем сервисам для замера времени их запуска
for service in $services; do
  # Ожидаем, пока сервис не станет "здоровым" или статус контейнера "running"
  while true; do
    status=$(docker inspect --format='{{.State.Health.Status}}' $(docker-compose ps -q $service) 2>/dev/null)
    if [ "$status" = "healthy" ]; then
      break
    fi
    # Если здоровье не проверяется, проверяем просто "running"
    running=$(docker inspect --format='{{.State.Running}}' $(docker-compose ps -q $service) 2>/dev/null)
    if [ "$running" = "true" ]; then
      break
    fi
    sleep 0.1
  done
  end_time=$(date +%s%3N)
  start_times[$service]=$((end_time - start_all))
done

# Вывод результатов
echo "Service startup time (ms):"
for service in "${!start_times[@]}"; do
  echo "$service : ${start_times[$service]}"
done
