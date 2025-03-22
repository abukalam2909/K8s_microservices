resource "google_artifact_registry_repository" "docker_repo" {
  provider      = google
  project       = var.project_id
  location      = var.region
  repository_id = "my-repo"
  description   = "Docker repository for my app"
  format        = "DOCKER"
}
