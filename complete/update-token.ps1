param (
    [string]$username = "user",
    [string]$password = "user",
    [string]$role = "token_user" # nebo "token_admin"
)

$clientId = "web-app"
$realm = "ea"
$keycloakUrl = "http://localhost:8091"

$response = Invoke-RestMethod -Method POST "$keycloakUrl/realms/$realm/protocol/openid-connect/token" `
    -ContentType "application/x-www-form-urlencoded" `
    -Body @{
        client_id = $clientId
        username = $username
        password = $password
        grant_type = "password"
    }

$token = $response.access_token

# Načti stávající JSON nebo vytvoř nový
$jsonPath = "./rest-client.env.json"
if (Test-Path $jsonPath) {
    $json = Get-Content $jsonPath | ConvertFrom-Json
} else {
    $json = @{ dev = @{} }
}

$json.dev.host = "http://localhost:8090"
$json.dev.$role = $token

# Ulož zpět
$json | ConvertTo-Json -Depth 10 | Set-Content $jsonPath -Encoding UTF8

Write-Host "  Token saved as '$role' in rest-client.env.json"

# .\update-token.ps1 -username "admin" -password "admin" -role "token_admin"
# .\update-token.ps1 -username "user" -password "user" -role "token_user"