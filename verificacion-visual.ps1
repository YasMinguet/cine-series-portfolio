# Script de verificacion visual final
Write-Host "`n============================================" -ForegroundColor Cyan
Write-Host "  VERIFICACION VISUAL FINAL" -ForegroundColor Cyan
Write-Host "============================================`n" -ForegroundColor Cyan

Write-Host "[1] Abriendo Eureka Dashboard..." -ForegroundColor Yellow
Start-Process "http://localhost:8761"
Start-Sleep 2

Write-Host "[2] Abriendo Swagger UI agregado (Gateway)..." -ForegroundColor Yellow
Start-Process "http://localhost:8080/swagger-ui.html"
Start-Sleep 2

Write-Host "[3] Abriendo Swagger UI User Service..." -ForegroundColor Yellow
Start-Process "http://localhost:8081/swagger-ui.html"
Start-Sleep 2

Write-Host "`n[4] Verificando estado de servicios...`n" -ForegroundColor Yellow

# Verificar Eureka
try {
    $eureka = Invoke-RestMethod "http://localhost:8761/eureka/apps" -TimeoutSec 5
    $apps = $eureka.applications.application
    Write-Host "    [OK] Eureka: $($apps.Count) servicios registrados" -ForegroundColor Green
} catch {
    Write-Host "    [FAIL] Eureka no responde" -ForegroundColor Red
}

# Verificar OpenAPI
$services = @{
    "8081" = "User"
    "8082" = "Series"
    "8083" = "Rating"
}

foreach ($port in $services.Keys) {
    try {
        $api = Invoke-RestMethod "http://localhost:$port/v3/api-docs" -TimeoutSec 3
        Write-Host "    [OK] OpenAPI $($services[$port]): v$($api.openapi)" -ForegroundColor Green
    } catch {
        Write-Host "    [FAIL] OpenAPI $($services[$port]): Error" -ForegroundColor Red
    }
}

Write-Host "`n[5] Test rapido de endpoint..." -ForegroundColor Yellow

try {
    $series = Invoke-RestMethod "http://localhost:8080/api/series" -TimeoutSec 5
    Write-Host "    [OK] GET /api/series: $($series.Count) series encontradas" -ForegroundColor Green
} catch {
    Write-Host "    [FAIL] GET /api/series: Error" -ForegroundColor Red
}

Write-Host "`n============================================" -ForegroundColor Cyan
Write-Host "  VERIFICACION COMPLETADA" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan

Write-Host "`nNavegadores abiertos con:" -ForegroundColor White
Write-Host "  - Eureka Dashboard (localhost:8761)" -ForegroundColor Gray
Write-Host "  - Swagger UI Gateway (localhost:8080/swagger-ui.html)" -ForegroundColor Gray
Write-Host "  - Swagger UI User Service (localhost:8081/swagger-ui.html)" -ForegroundColor Gray

Write-Host "`nVerifica visualmente que todo funcione correctamente.`n" -ForegroundColor Yellow

