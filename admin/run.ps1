# Descarga Maven localmente si no existe y corre el proyecto
$MVN_VERSION = "3.9.6"
$MVN_DIR = "$PSScriptRoot\.mvn-local\apache-maven-$MVN_VERSION"
$MVN_BIN = "$MVN_DIR\bin\mvn.cmd"
$MVN_ZIP = "$PSScriptRoot\.mvn-local\maven.zip"

if (-not (Test-Path $MVN_BIN)) {
    Write-Host "Maven no encontrado, descargando..." -ForegroundColor Cyan
    New-Item -ItemType Directory -Force -Path "$PSScriptRoot\.mvn-local" | Out-Null

    $urls = @(
        "https://archive.apache.org/dist/maven/maven-3/$MVN_VERSION/binaries/apache-maven-$MVN_VERSION-bin.zip",
        "https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/$MVN_VERSION/apache-maven-$MVN_VERSION-bin.zip"
    )

    $downloaded = $false
    foreach ($url in $urls) {
        try {
            Write-Host "Intentando: $url" -ForegroundColor Gray
            Invoke-WebRequest -Uri $url -OutFile $MVN_ZIP -UseBasicParsing -ErrorAction Stop
            $downloaded = $true
            break
        } catch {
            Write-Host "Fallo, intentando siguiente..." -ForegroundColor Yellow
        }
    }

    if (-not $downloaded) {
        Write-Host "ERROR: No se pudo descargar Maven." -ForegroundColor Red
        Write-Host "Descarga manualmente desde https://maven.apache.org/download.cgi" -ForegroundColor Red
        exit 1
    }

    Write-Host "Extrayendo Maven..." -ForegroundColor Cyan
    Expand-Archive -Path $MVN_ZIP -DestinationPath "$PSScriptRoot\.mvn-local" -Force
    Remove-Item $MVN_ZIP
    Write-Host "Maven listo." -ForegroundColor Green
}

Write-Host "Iniciando Spring Boot en http://localhost:8081 ..." -ForegroundColor Green
& $MVN_BIN "spring-boot:run"
