# 1. Login to get JWT
$baseUrl = "https://smart-inventory-management-system-for-j94f.onrender.com"
$login = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method Post -ContentType 'application/json' -Body '{"username":"admin","password":"admin123"}'
$token = $login.token
$headers = @{ "Authorization" = "Bearer $token"; "Content-Type" = "application/json" }

Write-Output ">>> Authenticated as admin."

# 2. Product definitions
$products = @(
    @{ productName = "pH Meter Model HI98130"; category = "Testing Instruments"; brand = "Hanna Instruments"; serials = @("PHM-HI98130-001", "PHM-HI98130-002", "PHM-HI98130-003") },
    @{ productName = "pH Meter Portable HI98103"; category = "Testing Instruments"; brand = "Hanna Instruments"; serials = @("PHM-HI98103-001", "PHM-HI98103-002") },
    @{ productName = "Benchtop pH Meter PH-500"; category = "Laboratory Equipment"; brand = "Jenway"; serials = @("PHM-PH500-001", "PHM-PH500-002") },
    @{ productName = "Digital Thermometer TM-902C"; category = "Measurement Instruments"; brand = "Lutron"; serials = @("THM-TM902-001", "THM-TM902-002", "THM-TM902-003") },
    @{ productName = "Infrared Thermometer GM300"; category = "Measurement Instruments"; brand = "Benetech"; serials = @("THM-GM300-001", "THM-GM300-002") },
    @{ productName = "Precision RTD Thermometer"; category = "Laboratory Equipment"; brand = "Fluke"; serials = @("THM-RTD-001", "THM-RTD-002") },
    @{ productName = "Temperature Controller TC-100"; category = "Industrial Equipment"; brand = "Autonics"; serials = @("TC-AUTNCS-001", "TC-AUTNCS-002", "TC-AUTNCS-003") },
    @{ productName = "PID Temperature Controller"; category = "Industrial Equipment"; brand = "Omega Engineering"; serials = @("TC-OMEGA-001", "TC-OMEGA-002") },
    @{ productName = "Thermohygrometer HT-1000"; category = "Environmental Monitoring"; brand = "Testo"; serials = @("THY-HT1000-001", "THY-HT1000-002", "THY-HT1000-003") },
    @{ productName = "Digital Thermo-Hygrometer 608"; category = "Environmental Monitoring"; brand = "Testo"; serials = @("THY-608-001", "THY-608-002") },
    @{ productName = "Digital Barometer DM-9601A"; category = "Environmental Monitoring"; brand = "Lutron"; serials = @("BAR-DM9601-001", "BAR-DM9601-002") },
    @{ productName = "Precision Barometer PTB110"; category = "Measurement Instruments"; brand = "Vaisala"; serials = @("BAR-PTB110-001") },
    @{ productName = "Universal Testing Machine UTM-50"; category = "Industrial Equipment"; brand = "Tinius Olsen"; serials = @("UTM-50-001", "UTM-50-002") },
    @{ productName = "Tensile Testing Machine TT-2000"; category = "Industrial Equipment"; brand = "Zwick Roell"; serials = @("TT-2000-001") },
    @{ productName = "Analytical Balance AB-204"; category = "Weighing Scales"; brand = "Mettler Toledo"; serials = @("WS-AB204-001", "WS-AB204-002") },
    @{ productName = "Precision Scale PS-3200"; category = "Weighing Scales"; brand = "Kern"; serials = @("WS-PS3200-001", "WS-PS3200-002") },
    @{ productName = "Industrial Platform Scale IPS"; category = "Weighing Scales"; brand = "A&D Weighing"; serials = @("WS-IPS-001", "WS-IPS-002") }
)

Write-Output ">>> Creating $($products.Count) products & serial numbers..."

foreach ($p in $products) {
    # Create product
    $body = @{ productName = $p.productName; category = $p.category; brand = $p.brand } | ConvertTo-Json
    $createdProduct = Invoke-RestMethod -Uri "$baseUrl/api/products" -Method Post -Headers $headers -Body $body
    $pId = $createdProduct.productId
    Write-Output "Created: $($p.productName) (ID: $pId)"

    # Stock-in serial numbers
    $stockInBody = @{
        productId = $pId
        serialNumbers = $p.serials
        referenceNote = "Initial stock intake - $($p.brand)"
    } | ConvertTo-Json

    $null = Invoke-RestMethod -Uri "$baseUrl/api/stock-in" -Method Post -Headers $headers -Body $stockInBody
}

Write-Output ">>> All 17 instruments and serial numbers successfully populated!"
