resource "google_artifact_registry_repository" "container1_repo" {
  provider      = google
  project       = var.project_id
  location      = var.region
  repository_id = "container1-repo"
  description   = "Docker repository for my app"
  format        = "DOCKER"
}

resource "google_artifact_registry_repository" "container2_repo" {
  provider      = google
  project       = var.project_id
  location      = var.region
  repository_id = "container2-repo"
  description   = "Docker repository for my app"
  format        = "DOCKER"
}
