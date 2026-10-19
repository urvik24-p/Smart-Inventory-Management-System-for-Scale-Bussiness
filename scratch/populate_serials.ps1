$baseUrl = "https://smart-inventory-management-system-for-j94f.onrender.com"
$login = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method Post -ContentType 'application/json' -Body '{"username":"admin","password":"admin123"}'
$token = $login.token
$headers = @{ "Authorization" = "Bearer $token"; "Content-Type" = "application/json" }

# Get existing products
$products = Invoke-RestMethod -Uri "$baseUrl/api/products" -Method Get -Headers $headers
Write-Output "Found $($products.Count) products in database."

# Map of product name prefix -> serial numbers
$serialMap = @{
    "pH Meter Model HI98130" = @("PHM-HI98130-001", "PHM-HI98130-002", "PHM-HI98130-003")
    "pH Meter Portable HI98103" = @("PHM-HI98103-001", "PHM-HI98103-002")
    "Benchtop pH Meter PH-500" = @("PHM-PH500-001", "PHM-PH500-002")
    "Digital Thermometer TM-902C" = @("THM-TM902-001", "THM-TM902-002", "THM-TM902-003", "THM-TM902-004")
    "Infrared Thermometer GM300" = @("THM-GM300-001", "THM-GM300-002")
    "Precision RTD Thermometer" = @("THM-RTD-001", "THM-RTD-002")
    "Temperature Controller TC-100" = @("TC-AUTNCS-001", "TC-AUTNCS-002", "TC-AUTNCS-003")
    "PID Temperature Controller" = @("TC-OMEGA-001", "TC-OMEGA-002")
    "Thermohygrometer HT-1000" = @("THY-HT1000-001", "THY-HT1000-002", "THY-HT1000-003")
    "Digital Thermo-Hygrometer 608" = @("THY-608-001", "THY-608-002")
    "Digital Barometer DM-9601A" = @("BAR-DM9601-001", "BAR-DM9601-002")
    "Precision Barometer PTB110" = @("BAR-PTB110-001")
    "Universal Testing Machine UTM-50" = @("UTM-50-001", "UTM-50-002")
    "Tensile Testing Machine TT-2000" = @("TT-2000-001")
    "Analytical Balance AB-204" = @("WS-AB204-001", "WS-AB204-002")
    "Precision Scale PS-3200" = @("WS-PS3200-001", "WS-PS3200-002")
    "Industrial Platform Scale IPS" = @("WS-IPS-001", "WS-IPS-002")
}

foreach ($prod in $products) {
    $targetId = [int]$prod.productId
    $name = $prod.productName
    if ($serialMap.ContainsKey($name)) {
        $serials = $serialMap[$name]
        $body = @{
            productId = $targetId
            serialNumbers = $serials
            referenceNote = "Initial Stock Intake - PO-$targetId"
        } | ConvertTo-Json

        try {
            $null = Invoke-RestMethod -Uri "$baseUrl/api/stock-in" -Method Post -Headers $headers -Body $body
            Write-Output "Stocked in $($serials.Count) units for: $name (ID: $targetId)"
        } catch {
            Write-Output "Error stocking in for $name : $_"
        }
    }
}

Write-Output ">>> All serials and stock-in transactions registered!"
