################################################################################
# EjercicioCopilot - Makefile for Docker Commands
################################################################################
#
# Facilita comandos comunes de Docker y Docker Compose
#
# Uso:
#   make help           - Mostrar todos los comandos disponibles
#   make build          - Construir imagen Docker
#   make up             - Iniciar contenedores con Docker Compose
#   make logs           - Ver logs en vivo
#   make down           - Detener contenedores
#   make clean          - Limpiar images y contenedores
#

.PHONY: help build up down logs ps shell clean rebuild push

################################################################################
# VARIABLES
################################################################################

IMAGE_NAME := ejerciciocopilot
IMAGE_TAG := latest
CONTAINER_NAME := ejerciciocopilot-app
REGISTRY := docker.io
REGISTRY_USER := tu_usuario

# Colores para output
BLUE := \033[0;34m
GREEN := \033[0;32m
RED := \033[0;31m
NC := \033[0m # No Color

################################################################################
# TARGETS - Docker Build
################################################################################

help:
	@echo "$(BLUE)EjercicioCopilot - Docker Commands$(NC)"
	@echo ""
	@echo "$(GREEN)Build:$(NC)"
	@echo "  make build              Construir imagen Docker"
	@echo "  make rebuild            Reconstruir sin caché"
	@echo "  make push               Pushear imagen a registry"
	@echo ""
	@echo "$(GREEN)Docker Compose:$(NC)"
	@echo "  make up                 Iniciar contenedores"
	@echo "  make down               Detener contenedores"
	@echo "  make down-volumes       Detener y eliminar volúmenes"
	@echo "  make restart            Reiniciar contenedores"
	@echo ""
	@echo "$(GREEN)Logs & Debugging:$(NC)"
	@echo "  make logs               Ver logs en vivo"
	@echo "  make ps                 Listar contenedores activos"
	@echo "  make shell              Entrar a shell del contenedor"
	@echo ""
	@echo "$(GREEN)Cleaning:$(NC)"
	@echo "  make clean              Limpiar images y contenedores"
	@echo "  make prune              Limpiar todo (images, contenedores, volúmenes)"
	@echo ""
	@echo "$(GREEN)Testing:$(NC)"
	@echo "  make test               Ejecutar tests en contenedor"
	@echo "  make health             Verificar health check"
	@echo ""

################################################################################
# Build Targets
################################################################################

build:
	@echo "$(BLUE)Construyendo imagen Docker...$(NC)"
	docker build -t $(IMAGE_NAME):$(IMAGE_TAG) .
	@echo "$(GREEN)✓ Imagen construida: $(IMAGE_NAME):$(IMAGE_TAG)$(NC)"

rebuild:
	@echo "$(BLUE)Reconstruyendo imagen sin caché...$(NC)"
	docker build --no-cache -t $(IMAGE_NAME):$(IMAGE_TAG) .
	@echo "$(GREEN)✓ Imagen reconstruida: $(IMAGE_NAME):$(IMAGE_TAG)$(NC)"

push:
	@echo "$(BLUE)Pusheando imagen a registry...$(NC)"
	docker tag $(IMAGE_NAME):$(IMAGE_TAG) $(REGISTRY)/$(REGISTRY_USER)/$(IMAGE_NAME):$(IMAGE_TAG)
	docker push $(REGISTRY)/$(REGISTRY_USER)/$(IMAGE_NAME):$(IMAGE_TAG)
	@echo "$(GREEN)✓ Imagen pushed a $(REGISTRY)$(NC)"

################################################################################
# Docker Compose Targets
################################################################################

up:
	@echo "$(BLUE)Iniciando contenedores con Docker Compose...$(NC)"
	docker-compose up -d
	@echo "$(GREEN)✓ Contenedores iniciados$(NC)"
	@echo "$(BLUE)Accesos:$(NC)"
	@echo "  API:       http://localhost:8080"
	@echo "  Swagger:   http://localhost:8080/swagger-ui.html"
	@echo "  H2:        http://localhost:8080/h2-console"
	@echo "  Health:    http://localhost:8080/actuator/health"

down:
	@echo "$(BLUE)Deteniendo contenedores...$(NC)"
	docker-compose down
	@echo "$(GREEN)✓ Contenedores detenidos$(NC)"

down-volumes:
	@echo "$(RED)Deteniendo contenedores y eliminando volúmenes...$(NC)"
	docker-compose down -v
	@echo "$(GREEN)✓ Contenedores y volúmenes eliminados$(NC)"

restart:
	@echo "$(BLUE)Reiniciando contenedores...$(NC)"
	docker-compose restart
	@echo "$(GREEN)✓ Contenedores reiniciados$(NC)"

################################################################################
# Logs & Debugging
################################################################################

logs:
	@echo "$(BLUE)Mostrando logs en vivo...$(NC)"
	docker-compose logs -f $(CONTAINER_NAME)

ps:
	@echo "$(BLUE)Contenedores activos:$(NC)"
	docker-compose ps

shell:
	@echo "$(BLUE)Entrando a shell del contenedor...$(NC)"
	docker-compose exec $(CONTAINER_NAME) /bin/sh

################################################################################
# Testing
################################################################################

test:
	@echo "$(BLUE)Ejecutando tests...$(NC)"
	docker-compose exec $(CONTAINER_NAME) mvn test
	@echo "$(GREEN)✓ Tests completados$(NC)"

health:
	@echo "$(BLUE)Verificando health check...$(NC)"
	@curl -s http://localhost:8080/actuator/health | jq '.' || echo "$(RED)Health check failed$(NC)"

################################################################################
# Cleaning
################################################################################

clean:
	@echo "$(BLUE)Limpiando images y contenedores...$(NC)"
	docker-compose down
	docker rmi $(IMAGE_NAME):$(IMAGE_TAG) 2>/dev/null || true
	@echo "$(GREEN)✓ Limpieza completada$(NC)"

prune:
	@echo "$(RED)Ejecutando docker system prune -a...$(NC)"
	docker system prune -a --volumes -f
	@echo "$(GREEN)✓ Sistema limpio$(NC)"

################################################################################
# Utility Targets
################################################################################

.SILENT: help
