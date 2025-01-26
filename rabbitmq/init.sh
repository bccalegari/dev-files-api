#!/bin/bash

echo "Starting RabbitMQ server..."
rabbitmq-server &

echo "Waiting for RabbitMQ to start..."
sleep 10

if [ ! -f /etc/rabbitmq/definitions.json ]; then
  echo "Importing definitions for the first time..."
  envsubst < /etc/rabbitmq/definitions.template.json > /etc/rabbitmq/definitions.json
  rabbitmqadmin import /etc/rabbitmq/definitions.json --username="${RABBITMQ_DEFAULT_USER}" --password="${RABBITMQ_DEFAULT_PASS}"
else
  echo "Definitions already imported, skipping."
fi

echo "RabbitMQ started successfully"
wait