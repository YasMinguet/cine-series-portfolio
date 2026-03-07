# 🔧 SOLUCIONAR PROBLEMAS CON DOCKER DESKTOP

Docker Desktop no se abre. Aquí están las **soluciones ordenadas por probabilidad**.

---

## ⚠️ Tienes instalado
```
Docker version: 29.2.1, build a5c7197
```

Pero el **servicio no está corriendo**.

---

## 🔍 Soluciones (En Orden de Prueba)

### **SOLUCIÓN 1: Reiniciar Windows (Más efectivo)**
**Tiempo:** 2 minutos

```
1. Abre PowerShell como ADMINISTRADOR
2. Ejecuta: Restart-Computer
3. Espera a que reinicie Windows
4. Abre Docker Desktop desde Inicio
5. Espera ~30 segundos a que cargue
```

**¿Por qué funciona?** A veces WSL 2 (que usa Docker) se queda en mal estado.

---

### **SOLUCIÓN 2: Iniciar Docker Service Manualmente**
**Tiempo:** 1 minuto

```powershell
# 1. Abre PowerShell como ADMINISTRADOR
# 2. Ejecuta estos comandos:

# Detener cualquier proceso Docker anterior
Stop-Process -Name "Docker Desktop" -Force -ErrorAction SilentlyContinue
Stop-Service -Name Docker -Force -ErrorAction SilentlyContinue

# Esperar 3 segundos
Start-Sleep -Seconds 3

# Iniciar el servicio Docker
Start-Service -Name Docker

# Esperar a que inicie
Start-Sleep -Seconds 5

# Verificar
docker --version
docker ps

# Si ves la salida sin errores, ¡Docker está corriendo!
```

**Resultado esperado:** Deberías ver la versión de Docker y una tabla vacía de contenedores.

---

### **SOLUCIÓN 3: Abrir Docker Desktop desde Terminal**
**Tiempo:** 1 minuto

```powershell
# Abre PowerShell como ADMINISTRADOR y ejecuta:

# Buscar Docker Desktop
$dockerPath = "C:\Program Files\Docker\Docker\Docker Desktop.exe"

# Si existe, abrirlo
if (Test-Path $dockerPath) {
    & $dockerPath
    Write-Host "Docker Desktop abierto. Espera 30 segundos..."
    Start-Sleep -Seconds 30
    
    # Verificar
    docker ps
} else {
    Write-Host "Docker Desktop no encontrado en la ruta esperada"
    # Buscar en otras ubicaciones
    Get-ChildItem -Path "C:\Program Files*" -Recurse -Filter "Docker.exe" -ErrorAction SilentlyContinue
}
```

---

### **SOLUCIÓN 4: Verificar WSL 2 (Requisito de Docker)**
**Tiempo:** 2 minutos

Docker Desktop en Windows requiere **WSL 2** (Windows Subsystem for Linux 2).

```powershell
# Verificar si WSL 2 está instalado
wsl --list --verbose

# Deberías ver algo como:
# NAME            STATE           VERSION
# * Ubuntu        Running         2
#   docker-desktop Running         2

# Si no ves nada, WSL 2 podría no estar instalado.
# En ese caso, instálalo:
wsl --install
```

Si WSL 2 no está instalado:
1. Abre PowerShell como **ADMINISTRADOR**
2. Ejecuta: `wsl --install`
3. Reinicia Windows
4. Prueba Docker Desktop nuevamente

---

### **SOLUCIÓN 5: Reinstalar Docker Desktop**
**Tiempo:** 10 minutos

Si nada funciona:

```powershell
# 1. Desinstalar completamente Docker Desktop
# Panel de Control → Programas → Desinstalar un programa
# Busca "Docker Desktop" y desinstálalo

# 2. Limpiar archivos residuales
Remove-Item -Path "$env:APPDATA\Docker" -Recurse -Force -ErrorAction SilentlyContinue
Remove-Item -Path "$env:USERPROFILE\.docker" -Recurse -Force -ErrorAction SilentlyContinue

# 3. Reiniciar Windows
Restart-Computer

# 4. Descargar e instalar Docker Desktop nuevamente
# https://www.docker.com/products/docker-desktop
```

---

## 🆘 Si Aún No Funciona

### Opción A: Usar Docker CLI Directamente (Sin GUI)

Si Docker CLI funciona pero Docker Desktop no, puedes usar docker-compose desde terminal:

```powershell
# Verificar que docker-compose está instalado
docker-compose --version

# Si funciona, puedes levantar tu proyecto sin Docker Desktop:
cd C:\Users\Kain\IdeaProjects\cine-series-portfolio\backend
docker-compose up --build
```

### Opción B: Usar Docker en WSL 2 Directamente

```powershell
# Entrar a WSL
wsl

# Navegar a tu proyecto (desde WSL)
cd /mnt/c/Users/Kain/IdeaProjects/cine-series-portfolio/backend

# Levantar Docker Compose
docker-compose up --build
```

---

## ✅ Verificación Final

Una vez que Docker esté corriendo, ejecuta esto:

```powershell
# Verificar que Docker está funcionando
docker ps

# Verificar que docker-compose está disponible
docker-compose --version

# Ir a tu proyecto
cd C:\Users\Kain\IdeaProjects\cine-series-portfolio\backend

# Levantar
docker-compose up --build

# En navegador (en otra ventana)
# http://localhost:8761
```

---

## 📊 Solución Recomendada (Orden)

1. **Intenta SOLUCIÓN 1** (Reiniciar) - Resuelve 70% de problemas
2. **Intenta SOLUCIÓN 2** (Iniciar servicio) - Resuelve 20% de problemas
3. **Intenta SOLUCIÓN 3** (Abrir desde terminal) - Resuelve 5% de problemas
4. **Intenta SOLUCIÓN 5** (Reinstalar) - Resuelve el 5% restante

---

## 💡 Mientras Esperas a Docker Desktop

Puedes:

```powershell
# 1. Compilar el proyecto (si aún no lo hiciste)
cd C:\Users\Kain\IdeaProjects\cine-series-portfolio\backend
C:\Tools\apache-maven-3.9.12\bin\mvn.cmd clean package -DskipTests

# 2. Leer la documentación
# Abre: README.md, CHECKLIST_FINAL.md, RECOMENDACIONES_MEJORAS.md

# 3. Preparar las mejoras que quieres hacer
# (Una vez Docker esté funcionando, podrás levantar y testear)
```

---

## 🎯 Próximo Paso

Intenta la **SOLUCIÓN 1** ahora:

```
1. Reinicia Windows
2. Abre Docker Desktop desde Inicio
3. Espera ~30 segundos
4. Verifica: docker ps
5. Avísame si funciona
```

---

**¡Cuéntame qué solución funciona o si necesitas ayuda adicional!** 🚀

