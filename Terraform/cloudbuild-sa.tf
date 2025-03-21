# Create a service account for Cloud Build
resource "google_service_account" "cloudbuild" {
  account_id   = "cloudbuild-sa"
  display_name = "Cloud Build Service Account"
}

# Grant permissions to the service account
resource "google_project_iam_member" "cloudbuild_secret_access" {
  project = var.project_id  # Add this line
  role    = "roles/secretmanager.secretAccessor"
  member  = "serviceAccount:${google_service_account.cloudbuild.email}"
}

resource "google_project_iam_member" "cloudbuild_storage_admin" {
  project = var.project_id  # Add this line
  role    = "roles/storage.admin"
  member  = "serviceAccount:${google_service_account.cloudbuild.email}"
}

resource "google_project_iam_member" "cloudbuild_k8s_developer" {
  project = var.project_id  # Add this line
  role    = "roles/container.developer"
  member  = "serviceAccount:${google_service_account.cloudbuild.email}"
}

resource "google_project_iam_member" "cloudbuild_artifact_registry" {
  project = var.project_id  # Add this line
  role    = "roles/artifactregistry.writer"
  member  = "serviceAccount:${google_service_account.cloudbuild.email}"
}
