Write-Host "[ATAQUE] Iniciando 50 peticiones al Catalogo..." -ForegroundColor Cyan
$exitoso = 0
$fallback = 0
$error = 0
$inicio = Get-Date

for ($i = 1; $i -le 50; $i++) {
    $response = curl.exe -s -w "`n%{http_code}" http://localhost:8081/catalog/product/123
    $lineas = $response -split "`n"
    $statusCode = $lineas[-1].Trim()
    $body = ($lineas[0..($lineas.Count - 2)]) -join "`n"
    
    if ($statusCode -eq "200") {
        if ($body -match "Fallback") {
            $fallback++
            Write-Host "  Peticion $i -> HTTP 200 (FALLBACK)" -ForegroundColor Yellow
        } else {
            $exitoso++
            Write-Host "  Peticion $i -> HTTP 200 (OK)" -ForegroundColor Green
        }
    } else {
        $error++
        Write-Host "  Peticion $i -> HTTP $statusCode (ERROR)" -ForegroundColor Red
    }
}

$fin = Get-Date
$duracion = ($fin - $inicio).TotalSeconds

Write-Host "`n[OK] Ataque finalizado en $([math]::Round($duracion, 2)) segundos" -ForegroundColor Green
Write-Host "RESUMEN DE ESTADISTICAS:" -ForegroundColor Cyan
Write-Host "  - Exitosas: $exitoso" -ForegroundColor Green
Write-Host "  - Fallback: $fallback" -ForegroundColor Yellow
Write-Host "  - Errores: $error" -ForegroundColor Red