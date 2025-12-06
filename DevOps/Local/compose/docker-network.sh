#!/bin/bash
set -e

NETWORK_NAME="fxqual-local-net"

case "$1" in
  create)
    echo "🔧 Creating Docker network: $NETWORK_NAME"
    if docker network ls | grep -q "$NETWORK_NAME"; then
      echo "ℹ️ Network already exists: $NETWORK_NAME"
    else
      docker network create "$NETWORK_NAME"
      echo "✅ Network created"
    fi
    ;;

  delete)
    echo "🗑 Removing Docker network: $NETWORK_NAME"
    if docker network ls | grep -q "$NETWORK_NAME"; then
      docker network rm "$NETWORK_NAME"
      echo "✅ Network removed"
    else
      echo "⚠️ Network does not exist"
    fi
    ;;

  status)
    echo "📡 Network status:"
    docker network ls | grep "$NETWORK_NAME" || echo "⚠️ Not created"
    ;;

  *)
    echo "Usage: $0 {create|delete|status}"
    exit 1
    ;;
esac
