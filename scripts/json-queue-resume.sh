#!/usr/bin/env bash
set -euo pipefail
: "${CONSUMER_JSON_BASE:=http://localhost:8083}"
curl -sS -f -X POST "${CONSUMER_JSON_BASE}/api/jms/consume/resume?listenerId=demo-pp-queue"
