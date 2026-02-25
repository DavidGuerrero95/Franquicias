# Infraestructura - Despliegue completo en nube (BONUS - Propuesto, no implementado)

Este apartado responde al **punto extra** del enunciado: *“Plus si toda la solución se despliega en la nube”*.

✅ En esta entrega **no se desplegó el microservicio completo en AWS** (API + runtime) por razones de costo.  
✅ Sí se desplegó la **persistencia en AWS (RDS MySQL)** usando **CloudFormation** y el proyecto corre localmente consumiendo esa BD y se simula secret manager desde localstack.

---

## ¿Por qué no se desplegó todo en la nube?

Aunque el despliegue en cloud es totalmente factible, **EKS + balanceadores + red privada** implica costos recurrentes inclusive si no hay tráfico:

- **EKS control plane** tiene costo por hora.
- Un **Application Load Balancer (ALB)** tiene costo por hora + consumo.
- Usar arquitectura correcta con **subnets privadas** usualmente requiere **NAT Gateway** (costo por hora + data).
- Logs/metrics (CloudWatch), almacenamiento (ECR), etc.

Como este repositorio es una **prueba técnica** (entorno temporal) y para evitar cargos inesperados, dejó documentado el diseño.

---

## Arquitectura propuesta (producción)

### Componentes AWS
- **ECR**: repositorio de imágenes Docker.
- **EKS**: cluster Kubernetes para ejecutar el microservicio.
- **Istio**: service mesh (mTLS, traffic management, observabilidad).
- **AWS Load Balancer Controller**: expone el servicio hacia internet usando **ALB**.
- **RDS MySQL**: persistencia (idealmente en subnets privadas).
- **Secrets Manager**: credenciales de BD y secretos.
- **External Secrets Operator** o CSI Driver para montar secretos en Kubernetes.
- **ACM** para TLS, **WAF** para capa adicional de seguridad.

### Flujo de tráfico (alto nivel)
Cliente -> **ALB (HTTPS)** -> **Istio Ingress Gateway** -> **Service (K8s)** -> **Pod MsFranchises** -> **RDS MySQL**

---

## Diseño de red recomendado

- VPC con:
  - **2 subnets públicas** (ALB)
  - **2 subnets privadas** (EKS nodes / pods)
  - **2 subnets privadas** (RDS)
- Security Groups:
  - ALB: inbound 443 desde internet
  - EKS: inbound desde ALB hacia Istio Ingress
  - RDS: inbound 3306 SOLO desde EKS SG (no público)

> En este repo, la RDS se dejó pública únicamente por facilidad en demo.

