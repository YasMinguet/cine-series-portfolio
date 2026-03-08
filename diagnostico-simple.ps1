# Script de diagnostico completo
$ErrorActionPreference = "SilentlyContinue"
$ProgressPreference = 'SilentlyContinue'

Write-Host "`n========================================"
Write-Host "  DIAGNOSTICO - MICROSERVICIOS"
Write-Host "========================================`n"

# CONTENEDORES
Write-Host "[1] CONTENEDORES"
Set-Location "C:\Users\Kain\IdeaProjects\cine-series-portfolio\backend"
docker compose ps
Write-Host ""

# EUREKA
Write-Host "[2] EUREKA DISCOVERY"
try {
    $eureka = Invoke-RestMethod "http://localhost:8761/eureka/apps" -TimeoutSec 5
    $apps = $eureka.applications.application
    Write-Host "    [OK] Servicios registrados: $($apps.Count)"
    foreach ($app in $apps) {
        Write-Host "         - $($app.name)"
    }
} catch {
    Write-Host "    [FAIL] Eureka no responde"
}
Write-Host ""

# HEALTH CHECKS
Write-Host "[3] HEALTH CHECKS"
@("8081", "8082", "8083", "8080") | ForEach-Object {
    try {
        $h = Invoke-RestMethod "http://localhost:$_/actuator/health" -TimeoutSec 3
        Write-Host "    [OK] Puerto $_ : $($h.status)"
    } catch {
        Write-Host "    [FAIL] Puerto $_ : DOWN"
    }
}
Write-Host ""

# OPENAPI
Write-Host "[4] OPENAPI"
@("8081", "8082", "8083") | ForEach-Object {
    try {
        $api = Invoke-RestMethod "http://localhost:$_/v3/api-docs" -TimeoutSec 3
        Write-Host "    [OK] Puerto $_ : OpenAPI v$($api.openapi)"
    } catch {
        Write-Host "    [FAIL] Puerto $_ : No disponible"
    }
}
Write-Host ""

# ENDPOINTS
Write-Host "[5] TESTS DE ENDPOINTS"
try {
    $user = "test_$(Get-Random)"
    $reg = @{username=$user;email="t@t.com";password="Test1234!"} | ConvertTo-Json
    $r = Invoke-RestMethod "http://localhost:8080/api/auth/register" -Method POST -Body $reg -ContentType "application/json" -TimeoutSec 10
    Write-Host "    [OK] Registro: ID $($r.userId)"

    $log = @{username=$user;password="Test1234!"} | ConvertTo-Json
    $l = Invoke-RestMethod "http://localhost:8080/api/auth/login" -Method POST -Body $log -ContentType "application/json" -TimeoutSec 10
    Write-Host "    [OK] Login: Token OK"

    $s = Invoke-RestMethod "http://localhost:8080/api/series" -TimeoutSec 5
    Write-Host "    [OK] Series: $($s.Count) encontradas"
} catch {
    Write-Host "    [FAIL] Error: $($_.Exception.Message)"
}

Write-Host "`n========================================"
Write-Host "       DIAGNOSTICO COMPLETADO"
Write-Host "========================================`n"

