# Create a service account for Cloud Build
resource "google_service_account" "cloudbuild" {
  account_id   = "cloudbuild-sa"
  display_name = "Cloud Build Service Account"
}

# Grant permissions to the service account
resource "google_project_iam_member" "cloudbuild_secret_access" {
  role   = "roles/secretmanager.secretAccessor"
  member = "serviceAccount:${google_service_account.cloudbuild.email}"
}

resource "google_project_iam_member" "cloudbuild_storage_admin" {
  role   = "roles/storage.admin"
  member = "serviceAccount:${google_service_account.cloudbuild.email}"
}

resource "google_project_iam_member" "cloudbuild_k8s_developer" {
  role   = "roles/container.developer"
  member = "serviceAccount:${google_service_account.cloudbuild.email}"
}

resource "google_project_iam_member" "cloudbuild_artifact_registry" {
  role   = "roles/artifactregistry.writer"
  member = "serviceAccount:${google_service_account.cloudbuild.email}"
}

# Create the Cloud Build trigger
resource "google_cloudbuild_trigger" "my_trigger" {
  name        = "my-cloudbuild-trigger"
  description = "Trigger to build and deploy my app"

  # GitHub source
  github {
    owner = "abukalam2909"
    name  = "K8s_microservices"
    push {
      branch = "main"  # Trigger on pushes to the main branch
    }

    # Use GitHub PAT for authentication
    secret {
      secret_name = "github-token"
      version_name = "latest"
    }
  }

  # Path to the cloudbuild.yaml file
  filename = "../cloudbuild.yaml"

  # Service account for Cloud Build
  service_account = google_service_account.cloudbuild.email
}