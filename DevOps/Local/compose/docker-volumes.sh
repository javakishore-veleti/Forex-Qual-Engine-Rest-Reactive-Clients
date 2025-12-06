#!/bin/bash
set -e

VOLUMES=(
  fxqual-kafka-data
  fxqual-zk-data
  fxqual-postgres-data
  fxqual-wiremock-data
  fxqual-grafana-data
  fxqual-prometheus-data
)

case "$1" in
  create)
    echo "Creating Docker volumes..."
    for vol in "${VOLUMES[@]}"; do
      if docker volume ls | grep -q "$vol"; then
        echo "Exists: $vol"
      else
        docker volume create "$vol"
        echo "Created: $vol"
      fi
    done
    ;;

  delete)
    echo "Deleting FXQUAL Docker volumes..."
    for vol in "${VOLUMES[@]}"; do
      if docker volume ls | grep -q "$vol"; then
        docker volume rm "$vol"
        echo "Deleted: $vol"
      else
        echo "Not found: $vol"
      fi
    done
    ;;

  prune)
    echo "Pruning unused volumes..."
    docker volume prune -f
    ;;

  status)
    echo "Volume status:"
    docker volume ls | grep fxqual || echo "⚠️ No FXQUAL volumes found"
    ;;

  *)
    echo "Usage: $0 {create|delete|prune|status}"
    exit 1
    ;;
esac
