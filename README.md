# 🎬 Cine Series Portfolio

> Proyecto portfolio de arquitectura de microservicios con Spring Boot y Spring Cloud

[![Estado](https://img.shields.io/badge/Estado-100%25%20Funcional-brightgreen)]()
[![E2E Smoke](https://img.shields.io/badge/E2E%20Smoke-8%2F8%20Passed-success)]()
[![Unit Tests](https://img.shields.io/badge/Unit%20Tests-62%20Passed-success)]()
[![Java](https://img.shields.io/badge/Java-17-orange)]()
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-green)]()
[![Docker](https://img.shields.io/badge/Docker-Ready-blue)]()

---

## 🚀 Quick Start

```powershell
# 1. Compilar
cd backend
mvn clean install

# 2. Levantar servicios
docker compose up -d

# 3. Esperar inicio (60 segundos)
Start-Sleep 60

# 4. Ejecutar test E2E/smoke (valida flujo completo)
cd ..
.\test-endpoints.ps1

# 5. Abrir Swagger UI
Start-Process http://localhost:8080/swagger-ui.html

# 6. Abrir Eureka Dashboard
Start-Process http://localhost:8761
```

**✅ Última validación:** 2026-03-08 - **8/8 tests pasados**

---

## 📋 Arquitectura

```
                    ┌─────────────────┐
                    │  API Gateway    │ :8080
                    │  (Swagger UI)   │
                    └────────┬────────┘
                             │
                   Eureka Load Balancing
                             │
           ┌─────────────────┼─────────────────┐
           │                 │                 │
           ▼                 ▼                 ▼
     ┌──────────┐      ┌──────────┐      ┌──────────┐
     │   User   │      │  Series  │      │  Rating  │
     │ Service  │      │ Service  │      │ Service  │
     │  :8081   │      │  :8082   │      │  :8083   │
     └──────────┘      └──────────┘      └──────────┘
           │                 │                 │
           └─────────────────┼─────────────────┘
                             │
                             ▼
                    ┌────────────────┐
                    │ Eureka Server  │ :8761
                    └────────────────┘
```

---

## 🛠️ Stack Tecnológico

### Backend
- **Spring Boot** 3.2.0
- **Spring Cloud** 2023.0.0
- **Java** 17 (Eclipse Temurin)
- **Maven** 3.9.12

### Microservicios
- **Spring Cloud Gateway** - API Gateway & Routing
- **Netflix Eureka** - Service Discovery
- **Spring Cloud Config** - Configuración centralizada
- **Spring Security** - Autenticación JWT
- **Spring Data JPA** - Persistencia
- **H2 Database** - Base de datos en memoria

### Documentación
- **Springdoc OpenAPI** 2.2.0
- **Swagger UI** - Documentación interactiva

### DevOps
- **Docker** - Containerización
- **Docker Compose** - Orchestración
- **Spring Actuator** - Monitoring

---

## 📦 Microservicios

| Servicio | Puerto | Descripción |
|----------|--------|-------------|
| Config Service | 8888 | Configuración centralizada |
| Discovery Service | 8761 | Service registry (Eureka) |
| Gateway Service | 8080 | API Gateway & Swagger UI |
| User Service | 8081 | Autenticación y usuarios |
| Series Service | 8082 | Gestión de series |
| Rating Service | 8083 | Sistema de valoraciones |

---

## 🔗 Enlaces

- 🌐 **Eureka Dashboard:** http://localhost:8761
- 📚 **Swagger UI:** http://localhost:8080/swagger-ui.html
- 🔧 **Gateway Health:** http://localhost:8080/actuator/health

---

## 🧪 Tests

El proyecto incluye **dos tipos de pruebas**:

1. **Tests unitarios (Maven)**: validan lógica por clase/capa (service, controller, handlers, etc.).
2. **Tests E2E/smoke (`test-endpoints.ps1`)**: validan el flujo completo de la API con servicios levantados.

### Tests unitarios (Maven)

```powershell
cd backend
mvn test
```

**Última ejecución verificada:** 62 tests unitarios pasados (`BUILD SUCCESS`) ✅

### Tests E2E / smoke (script de endpoints)

```powershell
.\test-endpoints.ps1
```

**Última ejecución verificada:** 8/8 pruebas E2E pasadas ✅

### Ejecutar tests unitarios por servicio

```powershell
cd backend
mvn -pl user-service test
mvn -pl series-service test
mvn -pl rating-service test
mvn -pl gateway-service test
mvn -pl discovery-service test
mvn -pl config-service test
```

### Diagnóstico rápido (estado de servicios)

```powershell
.\diagnostico-simple.ps1
```

---

## 📚 Documentación

### Documentos principales:
- 📚 **[INDICE_DOCUMENTACION.md](INDICE_DOCUMENTACION.md)** - Índice maestro (empieza aquí) ⭐
- 📊 **[INFORME_FINAL.md](INFORME_FINAL.md)** - Informe consolidado de validación
- 🔍 **[PROCESO_VALIDACION.md](PROCESO_VALIDACION.md)** - Timeline detallado de validación
- 📋 **[REPORTE_VALIDACION.md](REPORTE_VALIDACION.md)** - Reporte técnico completo
- 🔧 **[MEJORAS_RECOMENDADAS.md](MEJORAS_RECOMENDADAS.md)** - Guía de mejoras con código
- 🎯 **[ANALISIS_FALLOS.md](ANALISIS_FALLOS.md)** - Análisis de hallazgos (0 fallos)
- 📈 **[RESUMEN_EJECUTIVO.md](RESUMEN_EJECUTIVO.md)** - Para stakeholders
- 🎨 **[VALIDACION_VISUAL.txt](VALIDACION_VISUAL.txt)** - Resumen visual ASCII

### Scripts:
- 🧪 **test-endpoints.ps1** - Tests E2E completos (8 pruebas)
- 🔍 **diagnostico-simple.ps1** - Validación rápida
- 🌐 **verificacion-visual.ps1** - Abrir navegadores y validar

---

## 🎯 Endpoints Principales

### Autenticación (User Service)
- `POST /api/auth/register` - Registrar nuevo usuario
- `POST /api/auth/login` - Iniciar sesión (retorna JWT)

### Usuarios (User Service)
- `GET /api/users/{id}` - Obtener usuario por ID

### Series (Series Service)
- `GET /api/series` - Listar todas las series
- `GET /api/series/{id}` - Obtener serie por ID
- `POST /api/series` - Crear nueva serie (🔒 requiere auth)
- `PUT /api/series/{id}` - Actualizar serie (🔒 requiere auth)
- `DELETE /api/series/{id}` - Eliminar serie (🔒 requiere auth)

### Géneros (Series Service)
- `GET /api/series/genres` - Listar todos los géneros
- `POST /api/series/genres` - Crear nuevo género (🔒 requiere auth)

### Ratings (Rating Service)
- `GET /api/ratings` - Listar todas las calificaciones
- `GET /api/ratings/{id}` - Obtener calificación por ID
- `GET /api/ratings/series/{seriesId}` - Obtener calificaciones por serie
- `GET /api/ratings/user/{userId}` - Obtener calificaciones por usuario
- `POST /api/ratings` - Crear nueva calificación (🔒 requiere auth)
- `PUT /api/ratings/{id}` - Actualizar calificación (🔒 requiere auth)
- `DELETE /api/ratings/{id}` - Eliminar calificación (🔒 requiere auth)

---

## 🎓 Competencias Demostradas

- ✅ Arquitectura de Microservicios
- ✅ Spring Boot & Spring Cloud
- ✅ Service Discovery (Eureka)
- ✅ API Gateway Pattern
- ✅ RESTful API Design
- ✅ JWT Security
- ✅ Docker & Containerización
- ✅ OpenAPI/Swagger Documentation
- ✅ Health Monitoring

---

## 📊 Estado del Proyecto

**✅ 100% Funcional y Validado**

- ✅ Compilación: SUCCESS (26.5s)
- ✅ Docker: 6/6 contenedores UP
- ✅ Eureka: 4/4 servicios registrados
- ✅ Tests unitarios: 62 pasados
- ✅ Tests E2E/smoke: 8/8 pasados
- ✅ OpenAPI: 3/3 servicios documentados
- ✅ Health: 4/4 servicios UP

---

## 👤 Autor

**Yas Minguet**

---

## 📄 Licencia

Proyecto portfolio de demostración.

---

**⭐ Proyecto validado y funcional - Listo para portfolio ⭐**
