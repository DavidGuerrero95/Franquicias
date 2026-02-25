#!/usr/bin/env bash
set -euo pipefail

SECRET_NAME="${DB_SECRET_NAME:-ms-franchises/mysql}"

MYSQL_HOST="${MYSQL_HOST:-msfranchises-dev-mysql.cgvi8qwomstp.us-east-1.rds.amazonaws.com}"
MYSQL_PORT="${MYSQL_PORT:-3306}"
MYSQL_DB="${MYSQL_DB:-franchises}"
MYSQL_USER="${MYSQL_USER:-franchises_user}"
MYSQL_PASSWORD="${MYSQL_PASSWORD:-JRTAuAPxxRQc4f5lW1iK2d1gw4bAEYlI}"

SECRET_JSON=$(cat <<EOF
{"host":"${MYSQL_HOST}","port":${MYSQL_PORT},"dbname":"${MYSQL_DB}","username":"${MYSQL_USER}","password":"${MYSQL_PASSWORD}"}
EOF
)

if awslocal secretsmanager describe-secret --secret-id "${SECRET_NAME}" >/dev/null 2>&1; then
  awslocal secretsmanager put-secret-value \
    --secret-id "${SECRET_NAME}" \
    --secret-string "${SECRET_JSON}" >/dev/null
else
  awslocal secretsmanager create-secret \
    --name "${SECRET_NAME}" \
    --secret-string "${SECRET_JSON}" >/dev/null
fi

echo "Secret ready: ${SECRET_NAME}"