# Останавливаем и удаляем все контейнеры для холодного старта
docker-compose down

# Получаем список сервисов из docker-compose.yml
$services = docker-compose config --services

# Запускаем все сервисы
docker-compose up -d

# Функция для получения времени в миллисекундах
function Get-Timestamp {
    $unixEpoch = [DateTimeOffset]::new(1970,1,1,0,0,0,[TimeSpan]::Zero)
    $now = [DateTimeOffset]::UtcNow
    $ms = ($now - $unixEpoch).TotalMilliseconds
    return [int64]$ms
}

# Засекаем общее время старта
$startAll = Get-Timestamp

# Хеш-таблица для хранения времен старта сервисов
$startTimes = @{}

foreach ($service in $services) {
    while ($true) {
        $containerId = docker-compose ps -q $service
        if (-not $containerId) {
            Start-Sleep -Milliseconds 100
            continue
        }
        # Проверяем статус здоровья контейнера
        $health = docker inspect --format='{{.State.Health.Status}}' $containerId 2>$null
        if ($health -eq 'healthy') {
            break
        }
        # Если нет healthcheck, проверяем статус running
        $running = docker inspect --format='{{.State.Running}}' $containerId 2>$null
        if ($running -eq 'true') {
            break
        }
        Start-Sleep -Milliseconds 100
    }
    $endTime = Get-Timestamp
    $startTimes[$service] = $endTime - $startAll
}

Write-Output "Service startup time (ms):"
foreach ($key in $startTimes.Keys) {
    Write-Output "$key : $($startTimes[$key])"
}
