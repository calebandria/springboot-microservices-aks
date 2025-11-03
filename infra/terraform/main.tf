# infra/terraform/main.tf
terraform {
  required_version = ">= 1.5.0"

  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"   # CORRECT
      version = "~>3.0"
    }
    random = {
      source  = "hashicorp/random"
      version = "~>3.0"
    }
  }
}

provider "azurerm" {
  features {}
}

# ---------- Resource Group ----------
resource "azurerm_resource_group" "rg" {
  name     = "ecom-rg"
  location = "Qatar Central"
}

# ---------- Container Registry ----------
resource "azurerm_container_registry" "acr" {
  name                = "ecomacr${random_integer.suffix.result}"
  resource_group_name = azurerm_resource_group.rg.name
  location            = azurerm_resource_group.rg.location
  sku                 = "Basic"
  admin_enabled       = true
}

# ---------- Random suffix ----------
resource "random_integer" "suffix" {
  min = 1000
  max = 9999
}

# ---------- AKS Cluster ----------
resource "azurerm_kubernetes_cluster" "aks" {
  name                = "ecom-aks"
  location            = azurerm_resource_group.rg.location
  resource_group_name = azurerm_resource_group.rg.name
  dns_prefix          = "ecomaks"

  default_node_pool {
    name       = "agentpool"
    node_count = 1          # safer for free tier
    vm_size    = "Standard_DS2_v2"
  }

  identity {
    type = "SystemAssigned"
  }
}

# ---------- Grant AKS → ACR Pull ----------
resource "azurerm_role_assignment" "aks_acr" {   # TYPO FIXED
  principal_id                     = azurerm_kubernetes_cluster.aks.kubelet_identity[0].object_id
  role_definition_name             = "AcrPull"
  scope                            = azurerm_container_registry.acr.id   # ACR ID, not identity
  skip_service_principal_aad_check = true
}