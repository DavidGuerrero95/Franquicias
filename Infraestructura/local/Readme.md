# Infraestructura Local (MySQL + LocalStack)

Esta capa provee dependencias locales para ejecutar el microservicio:
- **MySQL 8.4** con esquema inicial (tablas franchise/branch/product)
- **LocalStack** con **Secrets Manager** para almacenar el secreto de conexión a DB
- Script de init que crea/actualiza el secreto automáticamente

> La forma recomendada de levantar todo es desde la **raíz del repo** con `docker compose up -d --build`.

---

## 📦 Componentes
### 1) MySQL
Ubicación:
- `Infraestructura/local/mysql/mysql-scripts/01_schema.sql`

Tablas:
- `franchise`
- `branch` (FK a `franchise`)
- `product` (FK a `branch`, check stock >= 0)

Puerto:
- Host: `3307`
- Container: `3306`

Credenciales (defaults en docker-compose raíz):
- user: `franchises_user`
- password: `${MYSQL_APP_PASSWORD:-admin}`
- database: `franchises`

---

### 2) LocalStack (Secrets Manager)
Servicios habilitados:
- `secretsmanager`

Puerto:
- `4566`

El secreto se crea en el arranque con el script:
- `Infraestructura/local/localstack/localstack-init/10-create-secret.sh`

Nombre del secreto (default):
- `ms-franchises/mysql`

Formato esperado:
```json
{
  "host": "mysql",
  "port": 3306,
  "dbname": "franchises",
  "username": "franchises_user",
  "password": "admin"
}
````

---

## 🧪 Verificar el secret en LocalStack

```bash
docker exec -it msfranchises-localstack awslocal secretsmanager list-secrets
docker exec -it msfranchises-localstack awslocal secretsmanager get-secret-value --secret-id ms-franchises/mysql
```

---

## 🧰 Script de init de LocalStack

Archivo:

* `Infraestructura/local/localstack/localstack-init/10-create-secret.sh`

Función:

* Construye el JSON con variables de entorno
* Si el secreto existe: `put-secret-value`
* Si no existe: `create-secret`

Variables usadas por el script:

* `DB_SECRET_NAME` (default `ms-franchises/mysql`)
* `MYSQL_HOST` (default `msfranchises-mysql` en script; en docker-compose raíz se inyecta `mysql`)
* `MYSQL_PORT` (default 3306)
* `MYSQL_DB` (default `franchises`)
* `MYSQL_USER` (default `franchises_user`)
* `MYSQL_PASSWORD` (default `admin`)

> En el `docker-compose.yml` de la raíz se inyectan valores para alinear host interno: `MYSQL_HOST=mysql`.

---

## 🧷 Permisos de scripts (Linux)

El compose raíz incluye un servicio `init-perms` que hace `chmod +x` sobre los scripts.

Si requieres manualmente:

```bash
chmod +x Infraestructura/local/localstack/localstack-init/*.sh
```

---

## 🧹 Limpieza

Apagar servicios:

```bash
docker compose down
```

Borrar volúmenes (incluye datos de MySQL):

```bash
docker compose down -v
```

---

## 🛠 Troubleshooting

### “bad substitution” o script no ejecuta

* Verifica que el script no tenga CRLF (Windows):

```bash
sed -i 's/\r$//' Infraestructura/local/localstack/localstack-init/10-create-secret.sh
```

### Secret no existe

* Revisa logs:

```bash
docker logs -f msfranchises-localstack
```

* Fuerza recreación de LocalStack:

```bash
docker compose up -d --force-recreate localstack
```