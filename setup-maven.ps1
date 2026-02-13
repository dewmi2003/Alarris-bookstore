# Maven Installation Script for Windows
# Run this script as Administrator

Write-Host "==================================================" -ForegroundColor Cyan
Write-Host "Maven Installation Script for Spring Boot Project" -ForegroundColor Cyan
Write-Host "==================================================" -ForegroundColor Cyan
Write-Host ""

# Configuration
$mavenVersion = "3.9.6"
$downloadUrl = "https://archive.apache.org/dist/maven/maven-3/$mavenVersion/binaries/apache-maven-$mavenVersion-bin.zip"
$installPath = "C:\Program Files\Apache\Maven"
$downloadPath = "$env:TEMP\apache-maven.zip"

# Check if running as administrator
$isAdmin = ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)

if (-not $isAdmin) {
    Write-Host "WARNING: This script should be run as Administrator for system-wide installation." -ForegroundColor Yellow
    Write-Host "Right-click PowerShell and select 'Run as Administrator', then run this script again." -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Attempting user-level installation instead..." -ForegroundColor Yellow
    $installPath = "$env:USERPROFILE\Maven"
}

try {
    # Create directory
    Write-Host "Creating installation directory..." -ForegroundColor Green
    if (-not (Test-Path $installPath)) {
        New-Item -ItemType Directory -Force -Path $installPath | Out-Null
    }

    # Download Maven
    Write-Host "Downloading Maven $mavenVersion..." -ForegroundColor Green
    Write-Host "URL: $downloadUrl" -ForegroundColor Gray
    Invoke-WebRequest -Uri $downloadUrl -OutFile $downloadPath -UseBasicParsing
    Write-Host "Download completed!" -ForegroundColor Green
    Write-Host ""

    # Extract
    Write-Host "Extracting Maven..." -ForegroundColor Green
    Expand-Archive -Path $downloadPath -DestinationPath $installPath -Force
    Write-Host "Extraction completed!" -ForegroundColor Green
    Write-Host ""

    # Set environment variables
    $mavenHome = "$installPath\apache-maven-$mavenVersion"
    Write-Host "Setting environment variables..." -ForegroundColor Green
    
    if ($isAdmin) {
        [Environment]::SetEnvironmentVariable("MAVEN_HOME", $mavenHome, "Machine")
        $currentPath = [Environment]::GetEnvironmentVariable("Path", "Machine")
        if ($currentPath -notlike "*$mavenHome\bin*") {
            [Environment]::SetEnvironmentVariable("Path", "$currentPath;$mavenHome\bin", "Machine")
        }
        Write-Host "MAVEN_HOME set to: $mavenHome (System)" -ForegroundColor Green
    } else {
        [Environment]::SetEnvironmentVariable("MAVEN_HOME", $mavenHome, "User")
        $currentPath = [Environment]::GetEnvironmentVariable("Path", "User")
        if ($currentPath -notlike "*$mavenHome\bin*") {
            [Environment]::SetEnvironmentVariable("Path", "$currentPath;$mavenHome\bin", "User")
        }
        Write-Host "MAVEN_HOME set to: $mavenHome (User)" -ForegroundColor Green
    }
    
    Write-Host ""

    # Cleanup
    Write-Host "Cleaning up..." -ForegroundColor Green
    Remove-Item $downloadPath -Force
    Write-Host ""

    # Success message
    Write-Host "==================================================" -ForegroundColor Cyan
    Write-Host "Maven installed successfully!" -ForegroundColor Green
    Write-Host "==================================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "IMPORTANT: Please CLOSE and REOPEN your terminal, then run:" -ForegroundColor Yellow
    Write-Host "  mvn -version" -ForegroundColor White
    Write-Host ""
    Write-Host "Installation location: $mavenHome" -ForegroundColor Gray
    Write-Host ""

} catch {
    Write-Host ""
    Write-Host "==================================================" -ForegroundColor Red
    Write-Host "Installation Failed!" -ForegroundColor Red
    Write-Host "==================================================" -ForegroundColor Red
    Write-Host "Error: $_" -ForegroundColor Red
    Write-Host ""
    Write-Host "Please try manual installation following the README.md guide." -ForegroundColor Yellow
}

Write-Host "Press any key to exit..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
