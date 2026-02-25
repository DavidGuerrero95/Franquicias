# MsFranchises - Documentación Técnica

Microservicio reactivo para gestionar **Franquicias**, **Sucursales** y **Productos**, incluyendo:
- CRUD (crear/renombrar/eliminar)
- Reporte: **Producto con mayor stock por sucursal** dentro de una franquicia
- Reporte: **Árbol de franquicias** con paginación y filtro por nombre de sucursal

---

## 🧱 Stack / Tecnologías
- Java 21
- Spring Boot 3.5.x
- Spring WebFlux (API reactiva)
- Spring Data R2DBC + r2dbc-mysql (acceso reactivo a MySQL)
- MySQL 8.4
- SpringDoc OpenAPI (Swagger UI)
- LocalStack (AWS Secrets Manager local)
- Docker / Docker Compose

---

## 🧭 Arquitectura (Clean / Hexagonal)
Estructura modular por capas:

- `domain/model`: modelos + gateways (interfaces) + DTOs de dominio
- `domain/usecase`: reglas de negocio (UseCases)
- `infrastructure/entry-points/reactive-web`: handlers WebFlux + rutas + validaciones + OpenAPI
- `infrastructure/driven-adapters/mysql-repository`: repositorios R2DBC + adapters
- `infrastructure/helpers/*`: commons, log, secrets-manager

### Flujo general
1. EntryPoint recibe request (WebFlux Handler)
2. Valida headers y body
3. Construye `Transaction` con request y metadata
4. Ejecuta UseCase
5. UseCase opera contra gateways (MySQL adapter / etc.)
6. Mapper construye response y se devuelve

---

## 🔐 Secrets Manager (credenciales de DB)
La conexión a MySQL **no usa credenciales en plano** dentro de la app.  
Se resuelve así:

- La app consulta `aws.secrets-manager.secretName` (por defecto: `ms-franchises/mysql`)
- LocalStack crea ese secreto al iniciar (script init)
- La app parsea el secreto a `DbSecret(host, port, dbname, username, password)`
- Se crea `ConnectionFactory` y `ConnectionPool` para R2DBC

### Formato del secreto esperado
```json
{
  "host": "mysql",
  "port": 3306,
  "dbname": "franchises",
  "username": "franchises_user",
  "password": "admin"
}
```

---

## ⚙️ Configuración principal (application.yaml)

Variables importantes:

* `PATH_BASE` (default `/ms-franchises/api/v1/`)
* AWS / Secrets Manager:
  * `AWS_REGION` (default `us-east-1`)
  * `AWS_SM_ENDPOINT` (default `http://localhost:4566`)
  * `AWS_ACCESS_KEY_ID` / `AWS_SECRET_ACCESS_KEY` (defaults `test`)
  * `DB_SECRET_NAME` (default `ms-franchises/mysql`)
* Pool MySQL (R2DBC):
  * `MYSQL_POOL_INITIAL` (default 2)
  * `MYSQL_POOL_MAX` (default 10)
  * `MYSQL_POOL_MAX_IDLE` (default 30m)
  * `MYSQL_POOL_MAX_LIFE` (default 60m)
  * `MYSQL_POOL_ACQUIRE_RETRY` (default 3)

---

## 📚 Swagger / OpenAPI

* Swagger UI: `http://localhost:8080/ms-franchises/api/v1/swagger-ui/index.html`
* OpenAPI JSON: `http://localhost:8080/ms-franchises/api/v1/api-docs`

---

## 🩺 Health endpoints

* `GET /ms-franchises/api/v1/health`
* `GET /ms-franchises/api/v1/health/liveness`
* `GET /ms-franchises/api/v1/health/readiness`

---

## 🧾 Reglas y validaciones

### Header requerido

* `message-id`: requerido por filtro para la mayoría de endpoints de negocio.

> Nota: el filtro ignora health y swagger (para permitir monitoreo/documentación).

### Validación de body

Se valida con Bean Validation.
Ejemplos:

* `name` obligatorio en create/rename
* `stock` obligatorio y no negativo

---

## 🔎 Endpoints (detalle)

### Franquicias

* `POST /franchises`
  * Body: `{ "data": { "name": "..." } }`
* `PUT /franchises/{franchiseId}/name`
  * Body: `{ "data": { "name": "..." } }`
* `GET /franchises/tree?pageNumber=1&pageSize=10&branchName=cen`
  * `branchName` solo aplica si length >= 3
* `GET /franchises/{franchiseId}/products/top-stock`

### Sucursales
* `POST /franchises/{franchiseId}/branches`
* `PUT /branches/{branchId}/name`

### Productos
* `POST /branches/{branchId}/products`
* `PUT /products/{productId}/name`
* `PUT /products/{productId}/stock`
* `DELETE /branches/{branchId}/products/{productId}`

---

## 🧯 Manejo de errores

Se usan excepciones de negocio y técnicas:

* `BusinessException` -> errores de validación o reglas (existencia, duplicados, etc.)
* `TechnicalException` -> fallas inesperadas, conexión DB, lectura de secretos, etc.

Códigos principales:

* Business: `MSB0xx`
* Technical: `MST0xx`

---

## 🐳 Docker (cómo se construye y ejecuta)

El Dockerfile (multi-stage):

1. Build con Gradle (genera `bootJar`)
2. Runtime sobre `eclipse-temurin:21-jre-alpine`
3. Healthcheck HTTP al endpoint `/health`

Para ejecución local se recomienda, usar el `docker-compose.yml` de la raíz del repo.
