# Franquicias - NEQUI Technical Test (MsFranchises)

Proyecto para la prueba técnica: Microservicio **MsFranchises** (Spring WebFlux + R2DBC + MySQL) con manejo de secretos vía **AWS Secrets Manager** simulado con **LocalStack** para entorno local.

---

## ✅ Ejecución local (solo 2 pasos)

### Requisitos
- Git
- Docker Engine / Docker Desktop
- Docker Compose v2 (`docker compose ...`)
- Puertos libres:
  - `8080` (API + Swagger)
  - `3307` (MySQL expuesto a tu máquina)
  - `4566` (LocalStack)

### 1) Clonar el repositorio
```bash
git clone https://github.com/DavidGuerrero95/Franquicias.git
cd Franquicias
````

### 2) Levantar todo con Docker Compose (desde la raíz)

```bash
docker compose up -d --build
```

Esto levanta:

* `msfranchises-mysql` (MySQL 8.4 con esquema inicial)
* `msfranchises-localstack` (Secrets Manager)
* `msfranchises-app` (MsFranchises API)

---

## 📚 Swagger / OpenAPI

Una vez arriba, el Swagger UI queda disponible en:

* **Swagger UI**: [http://localhost:8080/ms-franchises/api/v1/swagger-ui/index.html](http://localhost:8080/ms-franchises/api/v1/swagger-ui/index.html)

> Base path por defecto: `/ms-franchises/api/v1/`

---

## 🧪 Colección Postman

En `ColeccionAPIs/` se incluye:

* `MsFranchises - NEQUI Technical Test.postman_collection.json`

Pasos:

1. Importa la colección en Postman
2. Verifica variables:
   * `baseUrl = http://localhost:8080`
   * `basePath = ms-franchises/api/v1`
3. Ejecuta en orden: **Create Franchise -> Create Branch -> Create Product -> Top Stock / Tree**

---

## 🔎 Endpoints principales (resumen)

* Health: `GET /ms-franchises/api/v1/health`
* Franquicias:
  * `POST /ms-franchises/api/v1/franchises`
  * `PUT  /ms-franchises/api/v1/franchises/{franchiseId}/name`
  * `GET  /ms-franchises/api/v1/franchises/tree?pageNumber=1&pageSize=10&branchName=...`
  * `GET  /ms-franchises/api/v1/franchises/{franchiseId}/products/top-stock`
* Sucursales:
  * `POST /ms-franchises/api/v1/franchises/{franchiseId}/branches`
  * `PUT  /ms-franchises/api/v1/branches/{branchId}/name`
* Productos:
  * `POST   /ms-franchises/api/v1/branches/{branchId}/products`
  * `PUT    /ms-franchises/api/v1/products/{productId}/name`
  * `PUT    /ms-franchises/api/v1/products/{productId}/stock`
  * `DELETE /ms-franchises/api/v1/branches/{branchId}/products/{productId}`

### Header requerido

Para la mayoría de operaciones se requiere el header:

* `message-id: <uuid>`

---

## 🧰 Comandos útiles

### Ver estado

```bash
docker compose ps
```

### Logs

```bash
docker logs -f msfranchises-app
docker logs -f msfranchises-localstack
docker logs -f msfranchises-mysql
```

### Apagar

```bash
docker compose down
```

### Borrar todo (incluye datos MySQL)

```bash
docker compose down -v
```

---

## 🧩 Documentación por capas

* Microservicio (técnico): `MSFranchises/README.md`
* Infraestructura local: `Infraestructura/local/Readme.md`

---

## 🛠 Troubleshooting rápido

### 1) “Secret not found” (ResourceNotFoundException)

El microservicio lee credenciales de DB desde LocalStack (Secrets Manager) simulando el consumo de secretos de AWS.
Verifica que el secret exista:

```bash
docker exec -it msfranchises-localstack awslocal secretsmanager list-secrets
docker exec -it msfranchises-localstack awslocal secretsmanager get-secret-value --secret-id ms-franchises/mysql
```

Si no existe, revisa logs de LocalStack y el script:
`Infraestructura/local/localstack/localstack-init/10-create-secret.sh`

### 2) Puertos ocupados

Asegúrate de no tener servicios usando 8080/3307/4566 o cambia el mapeo en `docker-compose.yml`.
