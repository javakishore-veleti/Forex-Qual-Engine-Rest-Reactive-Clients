#!/bin/bash
set -e

BASE_DIR="$(cd "$(dirname "$0")" && pwd)"
COMPOSE_DIR="$BASE_DIR"

stop_stack() {
  local name=$1
  local file=$2
  echo "⬇️ Stopping $name..."
  docker compose -f "$file" down
}

stop_stack "Grafana"      "$COMPOSE_DIR/oltp-stack/Grafana/docker-compose.yml"
stop_stack "Prometheus"   "$COMPOSE_DIR/oltp-stack/Prometheus/docker-compose.yml"
stop_stack "OtelCollector" "$COMPOSE_DIR/oltp-stack/OtelCollector/docker-compose.yml"
stop_stack "Jaeger"       "$COMPOSE_DIR/oltp-stack/Jaeger/docker-compose.yml"
stop_stack "Kafka"        "$COMPOSE_DIR/kafka/docker-compose.yml"
stop_stack "WireMock"     "$COMPOSE_DIR/wiremock/docker-compose.yml"
stop_stack "Postgres"     "$COMPOSE_DIR/postgres/docker-compose.yml"

# remove network + volumes
sh "$BASE_DIR/docker-network.sh" delete
sh "$BASE_DIR/docker-volumes.sh" delete

echo "🛑 FXQUAL Local Environment Stopped"
