# 🔧 MEJORAS Y RECOMENDACIONES TÉCNICAS

## 📋 Índice
1. [Tests Unitarios](#tests-unitarios)
2. [Tests de Integración](#tests-de-integración)
3. [Monitoreo y Observabilidad](#monitoreo)
4. [Seguridad](#seguridad)
5. [Swagger UI](#swagger-ui)
6. [Config Service](#config-service)

---

## 1. Tests Unitarios

### ❌ Problema Actual
Ningún servicio tiene tests implementados:
```
[INFO] --- surefire:3.1.2:test (default-test) @ user-service ---
[INFO] No tests to run.
```

### ✅ Solución: Implementar Tests con JUnit 5 + Mockito

#### Ejemplo para User Service

**Archivo:** `backend/user-service/src/test/java/com/cinescope/user/service/AuthServiceTest.java`

```java
package com.cinescope.user.service;

import com.cinescope.user.model.User;
import com.cinescope.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@test.com");
        testUser.setPassword("$2a$10$hashed"); // BCrypt hash
    }

    @Test
    void registerUser_Success() {
        when(userService.registerUser(any())).thenReturn(testUser);
        when(jwtUtil.generateToken(any(), any())).thenReturn("mock-token");

        var result = authService.register(testUser);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertNotNull(result.getToken());
        verify(userService, times(1)).registerUser(any());
    }

    @Test
    void login_Success() {
        when(userRepository.findByUsername("testuser"))
            .thenReturn(Optional.of(testUser));
        when(jwtUtil.generateToken(anyString(), anyLong()))
            .thenReturn("mock-token");

        var result = authService.authenticate("testuser", "Password123!");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertNotNull(result.getToken());
    }

    @Test
    void login_InvalidUsername_ThrowsException() {
        when(userRepository.findByUsername("invalid"))
            .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> 
            authService.authenticate("invalid", "password")
        );
    }
}
```

#### Agregar dependencia de test en pom.xml

**Archivo:** `backend/user-service/pom.xml`

```xml
<dependencies>
    <!-- Existing dependencies -->
    
    <!-- Test dependencies -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-junit-jupiter</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

#### Tests para Series Service

**Archivo:** `backend/series-service/src/test/java/com/cinescope/series/service/SeriesServiceTest.java`

```java
package com.cinescope.series.service;

import com.cinescope.series.model.Series;
import com.cinescope.series.repository.SeriesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeriesServiceTest {

    @Mock
    private SeriesRepository seriesRepository;

    @InjectMocks
    private SeriesService seriesService;

    @Test
    void getAllSeries_ReturnsAllSeries() {
        Series s1 = new Series();
        s1.setId(1L);
        s1.setTitle("Breaking Bad");
        
        when(seriesRepository.findAll()).thenReturn(List.of(s1));

        List<Series> result = seriesService.getAllSeries();

        assertEquals(1, result.size());
        assertEquals("Breaking Bad", result.get(0).getTitle());
    }

    @Test
    void createSeries_Success() {
        Series newSeries = new Series();
        newSeries.setTitle("Better Call Saul");
        
        when(seriesRepository.save(any())).thenReturn(newSeries);

        Series result = seriesService.createSeries(newSeries);

        assertNotNull(result);
        assertEquals("Better Call Saul", result.getTitle());
        verify(seriesRepository, times(1)).save(any());
    }
}
```

---

## 2. Tests de Integración

### Test de integración con WebMvc

**Archivo:** `backend/user-service/src/test/java/com/cinescope/user/controller/AuthenticationControllerIntegrationTest.java`

```java
package com.cinescope.user.controller;

import com.cinescope.user.dto.AuthRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register_ValidUser_ReturnsToken() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setUsername("integration_test_" + System.currentTimeMillis());
        request.setEmail("test@integration.com");
        request.setPassword("Test1234!");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value(request.getUsername()));
    }

    @Test
    void login_InvalidCredentials_ReturnsBadRequest() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setUsername("nonexistent");
        request.setPassword("wrong");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
```

---

## 3. Monitoreo y Observabilidad

### ✅ Mejora: Exponer más endpoints de Actuator

Ya implementado en Gateway. Aplicar a otros servicios:

**Archivo:** `backend/user-service/src/main/resources/application.properties`

Agregar:
```properties
# Actuator endpoints
management.endpoints.web.exposure.include=health,info,metrics,prometheus
management.endpoint.health.show-details=always
management.metrics.export.prometheus.enabled=true

# Info
info.app.name=User Service
info.app.description=Servicio de usuarios y autenticación
info.app.version=1.0.0
```

### ✅ Mejora: Implementar Distributed Tracing

**Agregar en parent pom.xml:**
```xml
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-tracing-bridge-brave</artifactId>
</dependency>
<dependency>
    <groupId>io.zipkin.reporter2</groupId>
    <artifactId>zipkin-reporter-brave</artifactId>
</dependency>
```

**Configurar en cada servicio:**
```properties
# Tracing
management.tracing.sampling.probability=1.0
management.zipkin.tracing.endpoint=http://zipkin:9411/api/v2/spans
```

---

## 4. Seguridad

### ⚠️ Hallazgos Actuales

1. **JWT Secret hardcodeado:** No es recomendable en producción
2. **CORS:** No configurado, necesario si hay frontend
3. **Rate Limiting:** No implementado

### ✅ Mejora: Configurar CORS en Gateway

**Archivo:** `backend/gateway-service/src/main/java/com/cinescope/gateway/config/CorsConfig.java`

```java
package com.cinescope.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:4200"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}
```

### ✅ Mejora: Externalizar JWT Secret

**Archivo:** `backend/user-service/src/main/resources/application.properties`

```properties
# JWT Configuration
jwt.secret=${JWT_SECRET:default-secret-key-change-in-production}
jwt.expiration=86400000
```

**Archivo:** `backend/docker-compose.yml`

```yaml
user-service:
  build: ./user-service
  environment:
    - JWT_SECRET=your-super-secret-key-min-256-bits
  ports:
    - "8081:8081"
```

---

## 5. Swagger UI

### ✅ Estado Actual

**Springdoc inicializado correctamente:**
```
o.springdoc.api.AbstractOpenApiResource : Init duration for springdoc-openapi is: 76 ms (Gateway)
o.springdoc.api.AbstractOpenApiResource : Init duration for springdoc-openapi is: 529 ms (User Service)
```

### 📝 Verificación Manual Pendiente

Abrir en navegador para confirmar UI:
- http://localhost:8080/swagger-ui.html ← **Gateway agregado (todos los servicios)**
- http://localhost:8081/swagger-ui.html ← User Service individual
- http://localhost:8082/swagger-ui.html ← Series Service individual
- http://localhost:8083/swagger-ui.html ← Rating Service individual

### ✅ Mejora: Agregar metadata a los endpoints

**Archivo:** `backend/user-service/src/main/java/com/cinescope/user/controller/AuthenticationController.java`

```java
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "APIs para autenticación de usuarios")
public class AuthenticationController {

    @PostMapping("/register")
    @Operation(
        summary = "Registrar nuevo usuario",
        description = "Crea un nuevo usuario y retorna token JWT",
        responses = {
            @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o usuario ya existe")
        }
    )
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest request) {
        // ...existing code...
    }
}
```

---

## 6. Config Service

### ⚠️ Estado Actual

Config Service está corriendo pero **no tiene repositorio de configuración**.

### ✅ Mejora: Configurar repositorio Git o FileSystem

**Opción A: Repositorio Local**

**Archivo:** `backend/config-service/src/main/resources/application.properties`

```properties
server.port=8888
spring.application.name=config-service

# Configuración nativa (filesystem)
spring.cloud.config.server.native.search-locations=classpath:/config-repo

# Profile activo
spring.profiles.active=native
```

**Crear:** `backend/config-service/src/main/resources/config-repo/user-service.properties`

```properties
# User Service Configuration
spring.datasource.url=jdbc:h2:mem:userdb
jwt.secret=centralized-jwt-secret
logging.level.com.cinescope.user=DEBUG
```

**Crear:** `backend/config-service/src/main/resources/config-repo/series-service.properties`

```properties
# Series Service Configuration
spring.datasource.url=jdbc:h2:mem:seriesdb
logging.level.com.cinescope.series=DEBUG
```

**Opción B: Repositorio Git**

```properties
spring.cloud.config.server.git.uri=https://github.com/tu-usuario/config-repo
spring.cloud.config.server.git.default-label=main
spring.cloud.config.server.git.clone-on-start=true
```

### Configurar clientes para usar Config Service

**En cada servicio (user, series, rating), agregar a pom.xml:**

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```

**Crear:** `backend/user-service/src/main/resources/bootstrap.properties`

```properties
spring.application.name=user-service
spring.cloud.config.uri=http://config-service:8888
spring.cloud.config.fail-fast=true
spring.cloud.config.retry.max-attempts=5
```

---

## 7. Base de Datos Persistente

### ⚠️ Estado Actual

Todos los servicios usan **H2 en memoria** → datos se pierden al reiniciar.

### ✅ Mejora: PostgreSQL en Docker Compose

**Archivo:** `backend/docker-compose.yml`

```yaml
services:
  postgres:
    image: postgres:15-alpine
    environment:
      POSTGRES_DB: cinescope
      POSTGRES_USER: cinescope
      POSTGRES_PASSWORD: cinescope123
    ports:
      - "5432:5432"
    volumes:
      - postgres-data:/var/lib/postgresql/data
    networks:
      - microservices-net

  user-service:
    build: ./user-service
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/cinescope
      - SPRING_DATASOURCE_USERNAME=cinescope
      - SPRING_DATASOURCE_PASSWORD=cinescope123
      - SPRING_JPA_HIBERNATE_DDL_AUTO=update
    depends_on:
      - postgres
      - discovery-service
    # ...rest

volumes:
  postgres-data:
```

**Agregar en pom.xml de cada servicio:**

```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

---

## 8. Resiliencia

### ✅ Mejora: Circuit Breaker con Resilience4j

**Agregar en parent pom.xml:**

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-circuitbreaker-reactor-resilience4j</artifactId>
</dependency>
```

**Configurar en Gateway:**

```properties
# Circuit Breaker
resilience4j.circuitbreaker.instances.default.sliding-window-size=10
resilience4j.circuitbreaker.instances.default.failure-rate-threshold=50
resilience4j.circuitbreaker.instances.default.wait-duration-in-open-state=10000
```

---

## 9. Logging Centralizado

### ✅ Mejora: ELK Stack (Elasticsearch, Logstash, Kibana)

**Agregar a docker-compose.yml:**

```yaml
  elasticsearch:
    image: elasticsearch:8.11.0
    environment:
      - discovery.type=single-node
      - xpack.security.enabled=false
    ports:
      - "9200:9200"
    volumes:
      - elasticsearch-data:/usr/share/elasticsearch/data

  logstash:
    image: logstash:8.11.0
    volumes:
      - ./logstash/logstash.conf:/usr/share/logstash/pipeline/logstash.conf
    depends_on:
      - elasticsearch

  kibana:
    image: kibana:8.11.0
    ports:
      - "5601:5601"
    depends_on:
      - elasticsearch

volumes:
  elasticsearch-data:
```

**Configurar logback en cada servicio** para enviar logs a Logstash.

---

## 10. Validación de Datos

### ✅ Mejora: Bean Validation

**Agregar dependencia:**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

**Ejemplo en DTOs:**

```java
import jakarta.validation.constraints.*;

public class AuthRequest {
    @NotBlank(message = "Username es requerido")
    @Size(min = 3, max = 50, message = "Username debe tener entre 3 y 50 caracteres")
    private String username;

    @NotBlank(message = "Email es requerido")
    @Email(message = "Email debe ser válido")
    private String email;

    @NotBlank(message = "Password es requerido")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
             message = "Password debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial")
    private String password;
}
```

**En el controller:**

```java
@PostMapping("/register")
public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRequest request) {
    // ...
}
```

---

## 📊 PRIORIZACIÓN DE MEJORAS

### 🔴 CRÍTICAS (Implementar primero)
1. ✅ **Tests unitarios** - Cobertura mínima 70%
2. ✅ **Externalizar JWT secret** - Seguridad básica
3. ✅ **Validación de datos** - Prevenir inputs inválidos

### 🟡 IMPORTANTES (Implementar siguiente sprint)
4. ✅ **Base de datos persistente** - PostgreSQL
5. ✅ **CORS configurado** - Si hay frontend
6. ✅ **Circuit Breaker** - Resiliencia
7. ✅ **Config Service activo** - Centralizar config

### 🟢 OPCIONALES (Mejoras futuras)
8. ✅ **Distributed Tracing** - Zipkin/Jaeger
9. ✅ **Logging centralizado** - ELK Stack
10. ✅ **Tests de integración** - Cobertura E2E
11. ✅ **Rate limiting** - Protección DDoS
12. ✅ **API versioning** - /api/v1/...

---

## 🚀 COMANDOS RÁPIDOS

```powershell
# Compilar con tests
cd backend
mvn clean test

# Ver cobertura
mvn jacoco:report

# Verificar Swagger
Start-Process http://localhost:8080/swagger-ui.html

# Ver métricas de Actuator
Invoke-RestMethod http://localhost:8080/actuator/metrics

# Ver rutas del Gateway
Invoke-RestMethod http://localhost:8080/actuator/gateway/routes
```

---

**Siguiente paso recomendado:** Implementar tests unitarios en user-service como ejemplo piloto.

