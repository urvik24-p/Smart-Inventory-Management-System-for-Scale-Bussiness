try {
    $login = Invoke-RestMethod -Uri 'https://smart-inventory-management-system-for-j94f.onrender.com/api/auth/login' -Method Post -ContentType 'application/json' -Body '{"username":"admin","password":"admin123"}'
    Write-Output "Login successful. Token acquired."
    $headers = @{ "Authorization" = "Bearer " + $login.token }
    $products = Invoke-RestMethod -Uri 'https://smart-inventory-management-system-for-j94f.onrender.com/api/products' -Method Get -Headers $headers
    Write-Output "Products count: $($products.Count)"
    if ($products.Count -gt 0) {
        $products | Select-Object -First 5 productId, productName, category, brand | Format-Table
    } else {
        Write-Output "Products array is empty."
    }
} catch {
    Write-Output "Error: $_"
}
