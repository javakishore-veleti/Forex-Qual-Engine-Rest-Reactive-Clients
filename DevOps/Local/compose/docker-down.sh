#!/bin/bash
set -e

BASE_DIR="$(cd "$(dirname "$0")" && pwd)"
COMPOSE_DIR="$BASE_DIR"

stop_stack() {
  local name=$1
  local file=$2

  echo "Stopping $name..."
  docker compose -f "$file" down --remove-orphans 2>/dev/null || \
    echo "Nothing to stop for $name"
}

# Stop all stacks
stop_stack "Grafana"        "$COMPOSE_DIR/oltp-stack/Grafana/docker-compose.yml"
stop_stack "Prometheus"     "$COMPOSE_DIR/oltp-stack/Prometheus/docker-compose.yml"
stop_stack "OtelCollector"  "$COMPOSE_DIR/oltp-stack/OtelCollector/docker-compose.yml"
stop_stack "Jaeger"         "$COMPOSE_DIR/oltp-stack/Jaeger/docker-compose.yml"
stop_stack "Kafka"          "$COMPOSE_DIR/kafka/docker-compose.yml"
stop_stack "WireMock"       "$COMPOSE_DIR/wiremock/docker-compose.yml"
stop_stack "Postgres"       "$COMPOSE_DIR/postgres/docker-compose.yml"

# Remove network (safe)
echo "Removing network..."
sh "$BASE_DIR/docker-network.sh" delete || echo "Network already removed"

# Remove volumes (safe)
echo "Removing volumes..."
sh "$BASE_DIR/docker-volumes.sh" delete || echo "Some volumes already removed"

echo "FXQUAL Local Environment Stopped"
