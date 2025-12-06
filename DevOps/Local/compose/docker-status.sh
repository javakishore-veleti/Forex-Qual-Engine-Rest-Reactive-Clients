#!/bin/bash
echo "-------------------------------------------------------"
echo "📊 FX-QUAL Local Environment – Container Status"
echo "-------------------------------------------------------"

docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}" | \
  grep -E "fx|local|wiremock|postgres|grafana|jaeger|otel|kafka"

echo ""
echo "Run 'docker logs <container>' for details."
