# E-Commerce Backend – Microservices on Kubernetes

This repository contains a minimal two-service backend that demonstrates how to build, package, and deploy Java/Spring Boot microservices to Google Kubernetes Engine (GKE) using Cloud Build and Terraform.
Service 1 exposes an API for ingesting product data files and delegating computation; Service 2 parses the file and calculates product totals. A shared persistent volume stores uploaded files.

---

## Table of Contents

1. [Architecture](#architecture)  
2. [Repository Layout](#repository-layout)  
3. [Service APIs](#service-apis)  
4. [Prerequisites](#prerequisites)  
5. [Local Development](#local-development)  
6. [Container Images](#container-images)  
7. [Infrastructure](#infrastructure)  
8. [Kubernetes Deployment](#kubernetes-deployment)  
9. [Cloud Build Pipeline](#cloud-build-pipeline)  
10. [License](#license)

---

## Architecture

```
┌──────────────┐        POST /store-file        ┌──────────────┐
│  Client      │ ─────────────────────────────▶ │  container1  │
│              │        POST /calculate         │  (API svc)   │
└──────────────┘ ◀───────────────────────────── └─────┬────────┘
                                                     │
                                    HTTP (internal)  │
                                                     ▼
                                             ┌──────────────┐
                                             │  container2  │
                                             │ (compute svc)│
                                             └──────────────┘
```

* **container1**  
  * Spring Boot REST API (port **6000**).  
  * Persists uploaded files to a shared volume.  
  * Calls `container2` to compute product totals.  
* **container2**  
  * Spring Boot compute service (port **6001**).  
  * Reads CSV files from the shared volume and returns the sum of quantities for a given product.

Both services mount the same persistent volume at `/AbuKalamBabuji_PV_dir`.

---

## Repository Layout

| Path                | Description                                                   |
|---------------------|---------------------------------------------------------------|
| `container1/`       | Source for the API service.                                   |
| `container2/`       | Source for the compute service.                               |
| `kubernetes/`       | Kubernetes manifests (PV, PVC, Deployments, Services).        |
| `Terraform/`        | Terraform definitions for GCP resources (GKE, disk, IAM).     |
| `cloudbuild.yaml`   | Cloud Build pipeline to build images and deploy to GKE.       |

---

## Service APIs

### container1 – API Service

| Endpoint          | Method | Body Example                                                                               | Purpose                                       |
|-------------------|--------|--------------------------------------------------------------------------------------------|-----------------------------------------------|
| `/store-file`     | POST   | `{ "file": "products.csv", "data": "widget,5\nwidget,3\n..." }`                            | Writes the payload to `/AbuKalamBabuji_PV_dir` |
| `/calculate`      | POST   | `{ "file": "products.csv", "product": "widget" }`                                         | Calls container2 to compute totals             |

Environment variables used by container1:

* `COMPUTE_SERVICE_HOST` – hostname of container2 (e.g., `container2-service`).  
* `COMPUTE_SERVICE_PORT` – port of container2 (default `6001`).

### container2 – Compute Service

| Endpoint | Method | Body Example                                                | Purpose                                                         |
|----------|--------|-------------------------------------------------------------|-----------------------------------------------------------------|
| `/`      | POST   | `{ "file": "products.csv", "product": "widget" }`          | Parses CSV, sums quantities for the requested product.          |

Returns either `{ "sum": <number>, "file": "…" }` or `{ "error": "Input file not in CSV format." }`.

---

## Prerequisites

* Java **17** and Maven **3.8+**
* Docker
* Google Cloud SDK & authenticated account
* Terraform **1.0+**
* GKE cluster (can be created via provided Terraform code)

---

## Local Development

```bash
# Build jars
mvn clean package -f container1/pom.xml
mvn clean package -f container2/pom.xml

# Run services locally
java -jar container1/target/container1-0.0.1-SNAPSHOT.jar
java -jar container2/target/container2-0.0.1-SNAPSHOT.jar
```

Set `COMPUTE_SERVICE_HOST` and `COMPUTE_SERVICE_PORT` when running container1 so it can reach container2.

---

## Container Images

Each service has its own `Dockerfile`:

```bash
# Build
docker build -t container1:latest container1
docker build -t container2:latest container2

# Run
docker run -p 6000:6000 --name c1 container1:latest
docker run -p 6001:6001 --name c2 container2:latest
```

Mount a directory at `/AbuKalamBabuji_PV_dir` in both containers to emulate the shared volume.

---

## Infrastructure

Terraform scripts in `Terraform/` provision:

* A GKE cluster (`google_container_cluster`)
* Node pool (`google_container_node_pool`)
* Persistent disk (`google_compute_disk`)
* Artifact Registry repository
* Cloud Build service account with required IAM roles

Configure `terraform.tfvars` with your project ID and numbers, then:

```bash
cd Terraform
terraform init
terraform apply
```

---

## Kubernetes Deployment

Manifests are in `kubernetes/`:

1. `01-pv.yaml`, `02-pvc.yaml` – PersistentVolume & PersistentVolumeClaim  
2. `03-pod_1.yaml`, `04-pod_2.yaml` – Deployments for both containers (volume mounted, resources defined)  
3. `05-container1_service.yaml`, `06-container2_service.yaml` – Services (container1 exposed via LoadBalancer)

```bash
kubectl apply -f kubernetes/
```

The placeholders `IMAGE_TAG_CONTAINER1` and `IMAGE_TAG_CONTAINER2` must be replaced with actual image URIs before deployment (Cloud Build does this automatically).

---

## Cloud Build Pipeline

`cloudbuild.yaml` performs:

1. Maven build for each service  
2. Docker build and push to Artifact Registry  
3. Image tag substitution in manifests  
4. Deployment to the GKE cluster

Trigger the build in GCP Cloud Build or via:

```bash
gcloud builds submit --config cloudbuild.yaml .
```

---

## License

This project is provided as‑is for educational purposes. Adapt and extend it to suit your e‑commerce or microservices needs.

