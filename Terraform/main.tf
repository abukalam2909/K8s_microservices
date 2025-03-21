provider "google" {
  project = var.project_id
  region  = var.region
}

resource "google_container_cluster" "primary" {
  name     = var.cluster_name
  location = var.region

  networking_mode         = "VPC_NATIVE"  # Enables VPC-native (alias IP)
  remove_default_node_pool = true
  initial_node_count       = 1
}

resource "google_container_node_pool" "primary_nodes" {
  name       = "primary-node-pool"
  location   = var.region
  cluster    = google_container_cluster.primary.name

  node_count = 1  # Single node as required

  node_config {
    machine_type    = "e2-micro"       # 2 vCPU, 1GB memory
    image_type      = "COS_CONTAINERD" # Container-Optimized OS with containerd
    disk_type       = "pd-standard"    # Standard persistent disk
    disk_size_gb    = 10               # Boot disk size 10GB
    preemptible     = false            # Non-preemptible instance
    oauth_scopes    = ["https://www.googleapis.com/auth/cloud-platform"]
  }
}
