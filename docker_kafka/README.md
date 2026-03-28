# Docker Kafka + Spring Gateway + тестовый сервис

Этот шаблон поднимает Kafka, тестовый микросервис и gateway на Spring Boot.

## Быстрый старт

1) Откройте PowerShell в папке проекта.
2) Запустите:

```
docker compose up -d --build
```

Авто-проверка (поднимет стек, проверит HTTP и Kafka end-to-end):

```
python scripts/check.py --up
```

Проверка:

- Gateway -> сервис:
  - `http://localhost:8080/api/hello`
- Прямо сервис:
  - `http://localhost:8081/api/hello`
- Kafka (с хоста):
  - `localhost:9094` (внутри docker-сети остаётся `kafka:9092`)

Отправить сообщение в Kafka через gateway:

```
curl -X POST http://localhost:8080/api/messages -H "Content-Type: application/json" -d "{\"message\":\"hi\"}"
```

Остановить все контейнеры:

```
docker compose down
```

## Docker Desktop

1) Запустите Docker Desktop.
2) Выполните `docker compose up -d --build` в этой папке.
3) Откройте **Containers/Apps** — там появится проект с именем папки (например, `docker_kafka`).
4) Можно смотреть логи и перезапускать контейнеры прямо из UI.

Если видите ошибку про `//./pipe/dockerDesktopLinuxEngine`, обычно Docker Desktop не запущен или выбран не тот контекст. Быстрая проверка:

```
docker context use desktop-linux
docker version
```

## Visual Studio Code

- В VS Code: **File -> Open Folder** и выберите папку проекта.
- Или из PowerShell:

```
code .
```

Если команда `code` не работает, установите её через **View -> Command Palette -> Shell Command: Install 'code' command in PATH**.

## Как добавить новый микросервис

Самый быстрый вариант — скопировать папку `service` и поменять имя/порт.

1) Скопируйте `service` в, например, `service-orders`.
2) В `service-orders\pom.xml` поменяйте `artifactId` и `name`.
3) В `service-orders\src\main\resources\application.yml` поменяйте:
   - `server.port` (например, 8082)
   - `spring.application.name`
   - при желании `app.kafka.topic`
4) Добавьте новый сервис в `docker-compose.yml`:

```
  service-orders:
    build:
      context: ./service-orders
    container_name: service-orders
    ports:
      - "8082:8082"
    environment:
      - KAFKA_BOOTSTRAP_SERVERS=kafka:9092
    depends_on:
      - kafka
```

5) (Опционально) Добавьте маршрут в gateway (`gateway\src\main\resources\application.yml`), чтобы фронт ходил через него:

```
        - id: service-orders
          uri: http://service-orders:8082
          predicates:
            - Path=/api/orders/**
```

## Как запускать микросервисы

- Запустить или пересобрать все:

```
docker compose up -d --build
```

- Запустить/пересобрать только один сервис (например, новый):

```
docker compose up -d --build service-orders
```

- Посмотреть логи конкретного сервиса:

```
docker compose logs -f service-orders
```

- Перезапустить сервис:

```
docker compose restart service-orders
```
