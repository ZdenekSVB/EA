@echo off
echo Starting Docker containers...
docker-compose up -d

REM Wait until Keycloak is available
echo Waiting for Keycloak to start (http://localhost:8091/realms/ea)...

:wait_loop
powershell -Command "try { Invoke-WebRequest -UseBasicParsing -Uri 'http://localhost:8091/realms/ea/.well-known/openid-configuration' -TimeoutSec 2; exit 0 } catch { exit 1 }"
IF %ERRORLEVEL% NEQ 0 (
    timeout /t 2 >nul
    goto wait_loop
)

echo Keycloak is available.

REM Retrieve tokens
echo Fetching tokens from Keycloak...
powershell -ExecutionPolicy Bypass -File update-token.ps1 -username admin -password admin -role token_admin
powershell -ExecutionPolicy Bypass -File update-token.ps1 -username user -password user -role token_user

echo Done! All services are running and tokens are stored.
pause
