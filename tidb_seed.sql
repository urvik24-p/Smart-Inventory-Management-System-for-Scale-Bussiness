USE test;

-- Disable foreign key checks while setting up demo data
SET FOREIGN_KEY_CHECKS = 0;

-- Clear existing data
TRUNCATE TABLE stock_transaction_items;
TRUNCATE TABLE stock_transactions;
TRUNCATE TABLE serial_numbers;
TRUNCATE TABLE products;
TRUNCATE TABLE users;

-- ============================================================
-- 1. USERS
-- ============================================================
INSERT INTO users (user_id, username, password, role) VALUES
  (1, 'admin', '$2b$12$3MTmNw0Pl9xzzPYEj7mDAO6Urv1O/d.xkSaW2fFt3AA1qX1SbIUpK', 'ADMIN'),
  (2, 'staff1', '$2b$12$3MTmNw0Pl9xzzPYEj7mDAO6Urv1O/d.xkSaW2fFt3AA1qX1SbIUpK', 'STAFF');
-- Both passwords = admin123

-- ============================================================
-- 2. PRODUCTS (with explicit product_id so serial numbers match)
-- ============================================================
INSERT INTO products (product_id, product_name, category, brand) VALUES
  (1,  'pH Meter Model HI98130',        'Testing Instruments',       'Hanna Instruments'),
  (2,  'pH Meter Portable HI98103',     'Testing Instruments',       'Hanna Instruments'),
  (3,  'Benchtop pH Meter PH-500',      'Laboratory Equipment',      'Jenway'),
  (4,  'Digital Thermometer TM-902C',   'Measurement Instruments',   'Lutron'),
  (5,  'Infrared Thermometer GM300',    'Measurement Instruments',   'Benetech'),
  (6,  'Precision RTD Thermometer',     'Laboratory Equipment',      'Fluke'),
  (7,  'Temperature Controller TC-100', 'Industrial Equipment',      'Autonics'),
  (8,  'PID Temperature Controller',    'Industrial Equipment',      'Omega Engineering'),
  (9,  'Thermohygrometer HT-1000',      'Environmental Monitoring',  'Testo'),
  (10, 'Digital Thermo-Hygrometer 608', 'Environmental Monitoring',  'Testo'),
  (11, 'Digital Barometer DM-9601A',    'Environmental Monitoring',  'Lutron'),
  (12, 'Precision Barometer PTB110',    'Measurement Instruments',   'Vaisala'),
  (13, 'Universal Testing Machine UTM-50',  'Industrial Equipment',  'Tinius Olsen'),
  (14, 'Tensile Testing Machine TT-2000',   'Industrial Equipment',  'Zwick Roell'),
  (15, 'Analytical Balance AB-204',     'Weighing Scales',           'Mettler Toledo'),
  (16, 'Precision Scale PS-3200',       'Weighing Scales',           'Kern'),
  (17, 'Industrial Platform Scale IPS', 'Weighing Scales',           'A&D Weighing');

-- ============================================================
-- 3. SERIAL NUMBERS
-- ============================================================
INSERT INTO serial_numbers (product_id, serial_number, status) VALUES
  (1, 'PHM-HI98130-001', 'available'),
  (1, 'PHM-HI98130-002', 'available'),
  (1, 'PHM-HI98130-003', 'sold'),
  (2, 'PHM-HI98103-001', 'available'),
  (2, 'PHM-HI98103-002', 'available'),
  (3, 'PHM-PH500-001',   'available'),
  (3, 'PHM-PH500-002',   'damaged'),
  (4, 'THM-TM902-001',   'available'),
  (4, 'THM-TM902-002',   'available'),
  (4, 'THM-TM902-003',   'sold'),
  (4, 'THM-TM902-004',   'sold'),
  (5, 'THM-GM300-001',   'available'),
  (5, 'THM-GM300-002',   'available'),
  (6, 'THM-RTD-001',     'available'),
  (7, 'TC-AUTNCS-001',   'available'),
  (7, 'TC-AUTNCS-002',   'available'),
  (7, 'TC-AUTNCS-003',   'sold'),
  (8, 'TC-OMEGA-001',    'available'),
  (8, 'TC-OMEGA-002',    'available'),
  (9,  'THY-HT1000-001', 'available'),
  (9,  'THY-HT1000-002', 'available'),
  (9,  'THY-HT1000-003', 'sold'),
  (10, 'THY-608-001',    'available'),
  (10, 'THY-608-002',    'available'),
  (10, 'THY-608-003',    'sold'),
  (11, 'BAR-DM9601-001', 'available'),
  (11, 'BAR-DM9601-002', 'available'),
  (12, 'BAR-PTB110-001', 'available'),
  (13, 'TTM-UTM50-001',  'available'),
  (13, 'TTM-UTM50-002',  'sold'),
  (14, 'TTM-TT2000-001', 'available'),
  (15, 'WS-AB204-001',   'available'),
  (15, 'WS-AB204-002',   'available'),
  (15, 'WS-AB204-003',   'sold'),
  (16, 'WS-PS3200-001',  'available'),
  (16, 'WS-PS3200-002',  'available'),
  (17, 'WS-IPS-001',     'available'),
  (17, 'WS-IPS-002',     'available'),
  (17, 'WS-IPS-003',     'damaged');

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;
