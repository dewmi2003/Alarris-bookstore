# MySQL Password Reset Script
# Run this script as Administrator

Write-Host "==================================================" -ForegroundColor Cyan
Write-Host "MySQL Root Password Reset Script" -ForegroundColor Cyan
Write-Host "==================================================" -ForegroundColor Cyan
Write-Host ""

$newPassword = "admin123"

# Check if running as administrator
$isAdmin = ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)

if (-not $isAdmin) {
    Write-Host "ERROR: This script must be run as Administrator!" -ForegroundColor Red
    Write-Host "Right-click PowerShell and select 'Run as Administrator'" -ForegroundColor Yellow
    pause
    exit
}

try {
    Write-Host "Step 1: Stopping MySQL service..." -ForegroundColor Green
    Stop-Service -Name "MySQL80" -Force
    Start-Sleep -Seconds 5
    Write-Host "MySQL service stopped successfully." -ForegroundColor Green
    Write-Host ""

    Write-Host "Step 2: Creating password reset file..." -ForegroundColor Green
    $resetFile = "$env:TEMP\mysql-reset.txt"
    $resetSQL = "ALTER USER 'root'@'localhost' IDENTIFIED BY '$newPassword';"
    Set-Content -Path $resetFile -Value $resetSQL
    Write-Host "Reset file created: $resetFile" -ForegroundColor Gray
    Write-Host ""

    Write-Host "Step 3: Starting MySQL with reset option..." -ForegroundColor Green
    $mysqldPath = "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysqld.exe"
    $myIniPath = "C:\ProgramData\MySQL\MySQL Server 8.0\my.ini"
    
    # Start mysqld with init-file
    $process = Start-Process -FilePath $mysqldPath -ArgumentList "--defaults-file=`"$myIniPath`"", "--init-file=`"$resetFile`"", "--console" -PassThru -WindowStyle Hidden
    
    Write-Host "Waiting for password reset to complete..." -ForegroundColor Yellow
    Start-Sleep -Seconds 10
    
    Write-Host "Step 4: Stopping temporary MySQL process..." -ForegroundColor Green
    Stop-Process -Name "mysqld" -Force -ErrorAction SilentlyContinue
    Start-Sleep -Seconds 3
    
    Write-Host "Step 5: Starting MySQL service normally..." -ForegroundColor Green
    Start-Service -Name "MySQL80"
    Start-Sleep -Seconds 5
    
    $status = (Get-Service -Name "MySQL80").Status
    if ($status -eq "Running") {
        Write-Host "MySQL service started successfully." -ForegroundColor Green
    }
    
    # Clean up
    Remove-Item $resetFile -Force -ErrorAction SilentlyContinue
    
    Write-Host ""
    Write-Host "==================================================" -ForegroundColor Cyan
    Write-Host "Password Reset Complete!" -ForegroundColor Green
    Write-Host "==================================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "New MySQL credentials:" -ForegroundColor Yellow
    Write-Host "  Username: root" -ForegroundColor White
    Write-Host "  Password: $newPassword" -ForegroundColor White
    Write-Host ""
    Write-Host "Testing connection..." -ForegroundColor Yellow
    
    $mysqlPath = "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
    $testResult = & $mysqlPath -u root -p"$newPassword" -e "SELECT 'SUCCESS' AS Status;" 2>&1
    
    if ($testResult -like "*SUCCESS*") {
        Write-Host "Connection test PASSED!" -ForegroundColor Green
    } else {
        Write-Host "Connection test failed. You may need to manually verify." -ForegroundColor Yellow
    }
    
} catch {
    Write-Host ""
    Write-Host "==================================================" -ForegroundColor Red
    Write-Host "Error occurred during password reset!" -ForegroundColor Red
    Write-Host "==================================================" -ForegroundColor Red
    Write-Host "Error: $_" -ForegroundColor Red
    Write-Host ""
    Write-Host "Please ensure:" -ForegroundColor Yellow
    Write-Host "1. You are running as Administrator" -ForegroundColor Yellow
    Write-Host "2. No other MySQL processes are running" -ForegroundColor Yellow
    Write-Host "3. MySQL is properly installed" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "Press any key to exit..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
