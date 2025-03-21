resource "google_project_iam_binding" "artifact_registry_writer" {
  project = var.project_id
  role    = "roles/artifactregistry.writer"

  members = [
    "serviceAccount:${var.project_number}@cloudbuild.gserviceaccount.com"
  ]
}