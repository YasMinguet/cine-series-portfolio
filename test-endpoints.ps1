#!/usr/bin/env pwsh

# Script de testing exhaustivo - Versión simplificada
$baseUrl = "http://localhost:8080"
$ProgressPreference = 'SilentlyContinue'

Write-Output "`n========== TESTING EXHAUSTIVO ==========`n"

# Variables globales
$token = $null
$seriesId = $null
$testUsername = "testuser_$(Get-Random)"

# ============================================================================
# 1. DISCOVERY SERVICE
# ============================================================================
Write-Output "[1] DISCOVERY SERVICE (Puerto 8761)"
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8761/eureka/apps" -TimeoutSec 10
    Write-Output "    OK - Eureka activo`n"
} catch {
    Write-Output "    FAIL - Eureka no responde`n"
}

# ============================================================================
# 2. REGISTRO
# ============================================================================
Write-Output "[2] AUTENTICACION - REGISTRO"
$registerData = @"
{"username":"$testUsername","email":"test@cinescope.com","password":"Test1234!"}
"@

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/auth/register" -Method POST -Body $registerData -ContentType "application/json" -TimeoutSec 15
    $token = $response.token
    $userId = $response.userId
    Write-Output "    OK - Usuario registrado: $($response.username) (ID: $userId)`n"
} catch {
    Write-Output "    FAIL - Error: $($_.Exception.Response.StatusCode)`n"
}

# ============================================================================
# 3. LOGIN
# ============================================================================
Write-Output "[3] AUTENTICACION - LOGIN"
$loginData = @"
{"username":"$testUsername","password":"Test1234!"}
"@

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method POST -Body $loginData -ContentType "application/json" -TimeoutSec 15
    $token = $response.token
    Write-Output "    OK - Login exitoso`n"
} catch {
    Write-Output "    FAIL - Error: $($_.Exception.Response.StatusCode)`n"
}

# ============================================================================
# 4. LISTAR SERIES
# ============================================================================
Write-Output "[4] SERIES - LISTAR (GET /api/series)"
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/series" -TimeoutSec 15
    Write-Output "    OK - Series listadas: $($response.Count)`n"
} catch {
    Write-Output "    FAIL - Error: $($_.Exception.Response.StatusCode)`n"
}

# ============================================================================
# 5. CREAR SERIE
# ============================================================================
Write-Output "[5] SERIES - CREAR (POST /api/series, PROTEGIDO)"
$seriesData = @"
{"title":"Breaking Bad","description":"Meth kingpin","releaseYear":2008,"genre":"Drama"}
"@

$headers = @{ "Authorization" = "Bearer $token" }

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/series" -Method POST -Body $seriesData -Headers $headers -ContentType "application/json" -TimeoutSec 15
    $seriesId = $response.id
    Write-Output "    OK - Serie creada: $($response.title) (ID: $seriesId)`n"
} catch {
    Write-Output "    FAIL - Error: $($_.Exception.Response.StatusCode)`n"
}

# ============================================================================
# 6. OBTENER SERIE POR ID
# ============================================================================
Write-Output "[6] SERIES - GET POR ID (GET /api/series/{id})"
if ($seriesId) {
    try {
        $response = Invoke-RestMethod -Uri "$baseUrl/api/series/$seriesId" -TimeoutSec 15
        Write-Output "    OK - Serie obtenida: $($response.title)`n"
    } catch {
        Write-Output "    FAIL - Error: $($_.Exception.Response.StatusCode)`n"
    }
} else {
    Write-Output "    SKIP - Sin seriesId`n"
}

# ============================================================================
# 7. CREAR RATING
# ============================================================================
Write-Output "[7] RATINGS - CREAR (POST /api/ratings, PROTEGIDO)"
$ratingData = @"
{"userId":$userId,"seriesId":$seriesId,"score":5,"comment":"Excelente"}
"@
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/ratings" -Method POST -Body $ratingData -Headers $headers -ContentType "application/json" -TimeoutSec 15
    Write-Output "    OK - Rating creado: $($response.score)/5`n"
} catch {
    Write-Output "    FAIL - Error: $($_.Exception.Response.StatusCode)`n"
}

# ============================================================================
# 8. HEALTH CHECKS
# ============================================================================
Write-Output "[8] HEALTH CHECKS"
$services = @(
    @{ name = "user-service"; port = 8081 }
    @{ name = "series-service"; port = 8082 }
    @{ name = "rating-service"; port = 8083 }
)

foreach ($service in $services) {
    try {
        $response = Invoke-RestMethod -Uri "http://localhost:$($service.port)/actuator/health" -TimeoutSec 5
        Write-Output "    OK - $($service.name): $($response.status)"
    } catch {
        Write-Output "    FAIL - $($service.name): DOWN"
    }
}
Write-Output "`n"

Write-Output "========== FIN DEL TESTING ==========`n"










