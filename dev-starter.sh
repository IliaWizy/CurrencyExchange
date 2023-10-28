#!/bin/bash

echo "Сборка Docker-образа"
docker build -t exchange-db:latest -f ./scripts/db/Dockerfile .


if [ $? -ne 0 ]; then
  echo "Сборка Docker-образа завершилась с ошибкой. Прерываем выполнение скрипта."
  exit 1
fi


if [ -f .env.dev ]; then
  source .env.dev
else
  source .env
fi

echo "Запуск Docker-контейнера"

docker run -d -p 5432:5432 --rm \
  -e POSTGRES_DB="$DB_NAME" \
  -e POSTGRES_USER="$DB_USERNAME" \
  -e POSTGRES_PASSWORD="$DB_PASSWORD" \
  exchange-db:latest

if [ $? -ne 0 ]; then
  echo "Запуск Docker-контейнера завершился с ошибкой. Прерываем выполнение скрипта."
  exit 1
fi


echo "Docker-образ успешно собран и контейнер запущен."
