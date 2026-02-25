# Infraestructura - CloudFormation (RDS MySQL)

Este directorio contiene la infraestructura para desplegar la persistencia del proyecto en AWS:

- `msfranchises-persistence.yml`: plantilla CloudFormation para crear una **RDS MySQL pública** (modo demo), con cifrado KMS y un Secret en Secrets Manager.
- `scriptbd.sql`: script SQL para crear el esquema inicial (tablas `franchise`, `branch`, `product`).

> ⚠️ Seguridad: para demo la BD puede quedar expuesta (0.0.0.0/0).  
> Lo correcto es restringir el acceso por IP (`TU_IP_PUBLICA/32`).

---

## 🧱 ¿Qué crea la plantilla `msfranchises-persistence.yml`?

### Red (modo demo - subnets públicas)
- 1 VPC
- Internet Gateway + Route Table pública
- 2 Subnets públicas (2 AZ)
- DB Subnet Group con esas subnets

### Seguridad
- Security Group de RDS con inbound a **3306** desde `AllowedDbIngressCidr`

### Cifrado
- KMS Key + Alias (para cifrar RDS y Secrets Manager)

### Persistencia
- RDS MySQL (`PubliclyAccessible=true` por defecto)
- Base de datos inicial: `franchises` (por parámetro `DbName`)

### Secretos (AWS Secrets Manager)
- Secret `ms-franchises/mysql` (por parámetro `DbSecretName`)
- El secret genera `password` automáticamente y guarda estructura JSON:
```json
  {"host":"","port":3306,"dbname":"franchises","username":"franchises_user","password":"..."}
```

### Enriquecimiento automático del secret (endpoint real)

* Lambda Custom Resource (`SecretEnricherFn`) que:
* consulta el endpoint real de RDS (`describe_db_instances`)
* actualiza el secret con `host` y `port` reales

---

## ⚙️ Parámetros importantes

* `AllowedDbIngressCidr`: CIDR permitido para MySQL (ej: `x.x.x.x/32`)
* `DbName`: nombre de base de datos (default `franchises`)
* `DbUsername`: usuario master (default `franchises_user`)
* `DbInstanceClass`: default `db.t3.micro`
* `DbPubliclyAccessible`: default `true` (demo)
* `DbSecretName`: default `ms-franchises/mysql`

---

## 🚀 Despliegue de infraestructura

### Opción A) AWS Console (CloudFormation)

1. Ir a **CloudFormation > Create stack**
2. Upload template: `msfranchises-persistence.yml`
3. Stack name: por ejemplo `msfranchises-persistence-dev`
4. Parámetros:
   * `AllowedDbIngressCidr`: tu IP pública `/32` (recomendado) // Por efectos practicos no se configuro para esta prueba
5. Marcar: **CAPABILITY_NAMED_IAM**
6. Create stack

Luego, revisa **Outputs** del stack:

* `DbEndpointAddress`
* `DbEndpointPort`
* `DbSecretName`

---

### Opción B) CLI (AWS CLI)

Requisitos:

* AWS CLI configurado (`aws configure`)
* Permisos: CloudFormation, RDS, EC2 (VPC), Secrets Manager, IAM, KMS

Ejemplo:

```bash
aws cloudformation deploy \
  --region us-east-1 \
  --stack-name msfranchises-persistence-dev \
  --template-file Infraestructura/cloudformation/persistence/msfranchises-persistence.yml \
  --capabilities CAPABILITY_NAMED_IAM \
  --parameter-overrides \
    ProjectName=msfranchises \
    Environment=dev \
    DbSecretName=ms-franchises/mysql \
    DbPubliclyAccessible=true \
    AllowedDbIngressCidr=0.0.0.0/0
```

> Recomendado: cambiar `0.0.0.0/0` por `TU_IP_PUBLICA/32`.

---

## 🔎 Obtener credenciales y endpoint de la BD (AWS)

La plantilla deja el secret enriquecido con el endpoint real.

### 1) Obtener el secret desde AWS CLI

```bash
export AWS_REGION=us-east-1
export DB_SECRET_NAME=ms-franchises/mysql

aws --region "$AWS_REGION" secretsmanager get-secret-value \
  --secret-id "$DB_SECRET_NAME" \
  --query SecretString --output text
```

Debes ver JSON con `host`, `port`, `dbname`, `username`, `password`.

---

## 🗄️ Inicializar el schema (primera vez)

La RDS crea la base de datos (`DbName`), pero no crea las tablas.
Ejecuta `scriptbd.sql`.

### Opción A) Desde WSL/Ubuntu usando Docker (recomendado)

1. Exporta tus datos (puedes copiarlos del SecretString):

```bash
export RDS_HOST="ENDPOINT_RDS"
export RDS_PASSWORD="PASSWORD"
```

2. Aplica el SQL:

```bash
docker run --rm -i mysql:8.4 \
  mysql -h "$RDS_HOST" -P 3306 -u franchises_user -p"$RDS_PASSWORD" < Infraestructura/cloudformation/persistence/scriptbd.sql
```

---

### Opción B) Desde WSL/Ubuntu instalando mysql client

```bash
sudo apt update
sudo apt install -y mysql-client
```

Luego:

```bash
mysql -h ENDPOINT_RDS -P 3306 -u franchises_user -p < Infraestructura/cloudformation/persistence/scriptbd.sql
```

---

## 🧩 Conexión desde DBeaver (RDS)

Datos típicos:

* Host: `DbEndpointAddress`
* Port: `3306`
* Database: `franchises`
* User: `franchises_user`
* Password: desde el secret

### Si aparece: “Public Key Retrieval is not allowed”

En DBeaver:

* Edit Connection > Driver properties:

  * `allowPublicKeyRetrieval=true`
  * (opcional) `useSSL=true` (recomendado) o ajusta según tu driver/certificados

---

## 🔁 Sincronizar el secret AWS → LocalStack (opcional pero recomendado)

Tu proyecto usa LocalStack para simular Secrets Manager en local.
La idea es que el secreto en LocalStack sea el MISMO JSON de AWS.

Ejemplo (requiere AWS CLI configurado y docker corriendo):

```bash
export AWS_REGION=us-east-1
export DB_SECRET_NAME=ms-franchises/mysql

SECRET_JSON="$(aws --region "$AWS_REGION" secretsmanager get-secret-value \
  --secret-id "$DB_SECRET_NAME" --query SecretString --output text)"

docker exec -i msfranchises-localstack awslocal secretsmanager put-secret-value \
  --secret-id "$DB_SECRET_NAME" \
  --secret-string "$SECRET_JSON" >/dev/null

docker restart msfranchises-app
echo "✅ Secret sincronizado y app reiniciada."
```

---

## 🧨 Eliminación / limpieza

La RDS está con:

* `DeletionPolicy: Snapshot`
* `UpdateReplacePolicy: Snapshot`

Esto significa que al borrar el stack, AWS intentará dejar un snapshot.

Para borrar:

* CloudFormation > Delete stack
