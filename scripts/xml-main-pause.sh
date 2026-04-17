#!/usr/bin/env bash
set -euo pipefail
: "${CONSUMER_XML_BASE:=http://localhost:8084}"
curl -sS -f -X POST "${CONSUMER_XML_BASE}/api/jms/consume/pause"
