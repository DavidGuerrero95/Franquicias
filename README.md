# Franquicias - NEQUI Technical Test (MsFranchises)

Proyecto para la prueba técnica: Microservicio **MsFranchises** (Spring WebFlux + R2DBC + MySQL) con manejo de secretos vía **AWS Secrets Manager** simulado con **LocalStack** en local y BD desplegada en nube **AWS RDS**.

La base de datos se ejecuta en **AWS RDS MySQL** (creada con CloudFormation) y el microservicio se conecta a ella usando un **secret en LocalStack** con los datos de conexión a la BD en la nube.

---

## 🧱 Arquitectura (modo demo)

- **MsFranchises (local)**: contenedor Docker con la API.
- **LocalStack (local)**: simula AWS Secrets Manager.
- **AWS RDS MySQL (cloud)**: base de datos real.
- **Secret (LocalStack)**: contiene `host/port/dbname/username/password` de **RDS**.

> El microservicio lee el secreto desde LocalStack (`AWS_SM_ENDPOINT=http://localstack:4566`) y se conecta a RDS.

---

## ✅ Requisitos

- Git
- Docker Engine / Docker Desktop
- Docker Compose v2 (`docker compose ...`)
- Puertos libres:
  - `8080` (API + Swagger)
  - `4566` (LocalStack)

Además:
- Tener desplegada la BD en AWS (ver `Infraestructura/cloudformation/persistence/README.md`)
- Tener permitido el acceso a MySQL en el Security Group de RDS desde tu IP, para efectos practicos de la prueba se permite trafico a toda la red.

---

## 🚀 Ejecución local (2 pasos)

### 1) Clonar repositorio

```bash
git clone https://github.com/DavidGuerrero95/Franquicias.git
cd Franquicias
````

### Nota: se requeririea crear un archivo `.env` en la raíz

Crea un archivo `.env` (en la **raíz**, al lado del `docker-compose.yml`) con (por efectos practicos de la prueba se deja el archivo en el projecto):

```env
RDS_HOST=msfranchises-dev-mysql.xxxxxx.us-east-1.rds.amazonaws.com
RDS_PASSWORD=TU_PASSWORD
```

> `RDS_HOST` = endpoint de RDS
> `RDS_PASSWORD` = password del usuario

### 2) Levantar todo con Docker Compose desde la raiz del proyecto

```bash
docker compose up -d --build
```

Esto levanta:

* `msfranchises-localstack` (Secrets Manager simulado)
* `msfranchises-app` (MsFranchises API)

> El script `Infraestructura/local/localstack/localstack-init/10-create-secret.sh` crea/actualiza el secreto
> `ms-franchises/mysql` dentro de LocalStack con los valores de tu `.env` (RDS).

---

## 🗄️ Crear tablas en RDS (solo la primera vez, en este caso no es necesario)

La instancia RDS crea la **base de datos** (`franchises`), pero **no crea tablas**.
Para inicializar el schema, ejecuta `Infraestructura/cloudformation/persistence/scriptbd.sql`.

### Opción recomendada (sin instalar mysql): usando contenedor mysql client

```bash
docker run --rm -i mysql:8.4 \
  mysql -h "$RDS_HOST" -P 3306 -u franchises_user -p"$RDS_PASSWORD" < Infraestructura/cloudformation/persistence/scriptbd.sql
```

✅ Con esto quedan creadas las tablas: `franchise`, `branch`, `product`.

---

## 📚 Swagger / OpenAPI

Swagger UI:

* [http://localhost:8080/ms-franchises/api/v1/swagger-ui/index.html](http://localhost:8080/ms-franchises/api/v1/swagger-ui/index.html)

Base path por defecto: `/ms-franchises/api/v1/`

---

## 🧪 Colección Postman

En `ColeccionAPIs/`:

* `MsFranchises - NEQUI Technical Test.postman_collection.json`

Pasos:

1. Importa la colección en Postman
2. Variables:

   * `baseUrl = http://localhost:8080`
   * `basePath = ms-franchises/api/v1`
3. Ejecuta en orden:
   **Create Franchise -> Create Branch -> Create Product -> Top Stock / Tree**

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

Para la mayoría de operaciones se requiere:

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
```

### Apagar

```bash
docker compose down
```

---

## 🔐 Verificar el secreto que está usando LocalStack

```bash
docker exec -it msfranchises-localstack awslocal secretsmanager get-secret-value \
  --secret-id ms-franchises/mysql --query SecretString --output text
```

Debe mostrar algo como:

```json
{"host":"<RDS_HOST>","port":3306,"dbname":"franchises","username":"franchises_user","password":"***"}
```

---

## 🛠 Troubleshooting

### 1) “Secret not found” (ResourceNotFoundException)

Verifica que el secreto exista:

```bash
docker exec -it msfranchises-localstack awslocal secretsmanager list-secrets
docker exec -it msfranchises-localstack awslocal secretsmanager get-secret-value --secret-id ms-franchises/mysql
```

Si no existe, revisa:

* `Infraestructura/local/localstack/localstack-init/10-create-secret.sh`
* logs de LocalStack: `docker logs -f msfranchises-localstack`

### 2) Cambié el `.env` y sigue conectando a otra BD

El pool de conexiones se crea al arrancar el microservicio.
Reinicia:

```bash
docker restart msfranchises-app
```

### 3) No puedo conectarme a RDS desde mi PC

Revisa el **Security Group** del RDS:

* Inbound rule: TCP 3306 desde tu IP pública (`x.x.x.x/32`) (recomendado).
* Para demo se puede `0.0.0.0/0`, pero es inseguro.

---

## 📦 Documentación por capas

* Infraestructura CloudFormation (RDS): `Infraestructura/cloudformation/persistence/README.md`
* Infraestructura LocalStack: `Infraestructura/local/README.md`
* Microservicio (técnico): `MSFranchises/README.md`
