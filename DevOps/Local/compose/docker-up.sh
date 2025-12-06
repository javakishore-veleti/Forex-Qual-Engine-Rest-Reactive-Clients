#!/bin/bash
set -e

BASE_DIR="$(cd "$(dirname "$0")" && pwd)"

# Ensure network + volumes
sh "$BASE_DIR/docker-network.sh" create
sh "$BASE_DIR/docker-volumes.sh" create

COMPOSE_DIR="$BASE_DIR"

echo "🚀 Starting FXQUAL Local Environment..."

start_stack() {
  local name=$1
  local file=$2
  echo "➡️ Starting $name..."
  docker compose -f "$file" up -d
}

start_stack "Postgres"     "$COMPOSE_DIR/postgres/docker-compose.yml"
start_stack "WireMock"     "$COMPOSE_DIR/wiremock/docker-compose.yml"
start_stack "Kafka"        "$COMPOSE_DIR/kafka/docker-compose.yml"
start_stack "Jaeger"       "$COMPOSE_DIR/oltp-stack/Jaeger/docker-compose.yml"
start_stack "OtelCollector" "$COMPOSE_DIR/oltp-stack/OtelCollector/docker-compose.yml"
start_stack "Prometheus"   "$COMPOSE_DIR/oltp-stack/Prometheus/docker-compose.yml"
start_stack "Grafana"      "$COMPOSE_DIR/oltp-stack/Grafana/docker-compose.yml"

echo "🎉 All services started!"
