-- ============================================================
--  SmartInventory — SAMPLE DATA for Presentation
--  Scientific / Precision Instrument Business
--
--  Products: pH Meters, Thermometers, Temperature Controllers,
--            Thermohygrometers, Barometers,
--            Tensile Testing Machines, Weighing Scales
--
--  HOW TO RUN:
--    Option A (MySQL Workbench):
--      Open this file → File > Open SQL Script → Execute (⚡)
--
--    Option B (Command Line):
--      Windows CMD:
--        "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -p < sample_data.sql
--
--      PowerShell:
--        Get-Content "c:\Inventory sem 3\smartinventory\sample_data.sql" | & "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -purvik2407
--
--  NOTE: Run rebuild_db.sql FIRST if starting fresh.
-- ============================================================

USE inventory;

-- Clear existing data (for fresh demo)
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE stock_transaction_items;
TRUNCATE TABLE stock_transactions;
TRUNCATE TABLE serial_numbers;
TRUNCATE TABLE products;
TRUNCATE TABLE users;
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- 1. USERS
-- ============================================================
INSERT INTO users (username, password, role) VALUES
  ('admin', '$2b$12$3MTmNw0Pl9xzzPYEj7mDAO6Urv1O/d.xkSaW2fFt3AA1qX1SbIUpK', 'ADMIN'),
  ('staff1','$2b$12$3MTmNw0Pl9xzzPYEj7mDAO6Urv1O/d.xkSaW2fFt3AA1qX1SbIUpK', 'STAFF');
-- Both passwords = admin123

-- ============================================================
-- 2. PRODUCTS
-- ============================================================
INSERT INTO products (product_name, category, brand) VALUES
  -- pH Meters (product_id = 1,2,3)
  ('pH Meter Model HI98130',        'Testing Instruments',       'Hanna Instruments'),
  ('pH Meter Portable HI98103',     'Testing Instruments',       'Hanna Instruments'),
  ('Benchtop pH Meter PH-500',      'Laboratory Equipment',      'Jenway'),

  -- Thermometers (4,5,6)
  ('Digital Thermometer TM-902C',   'Measurement Instruments',   'Lutron'),
  ('Infrared Thermometer GM300',    'Measurement Instruments',   'Benetech'),
  ('Precision RTD Thermometer',     'Laboratory Equipment',      'Fluke'),

  -- Temperature Controllers (7,8)
  ('Temperature Controller TC-100', 'Industrial Equipment',      'Autonics'),
  ('PID Temperature Controller',    'Industrial Equipment',      'Omega Engineering'),

  -- Thermohygrometers (9,10)
  ('Thermohygrometer HT-1000',      'Environmental Monitoring',  'Testo'),
  ('Digital Thermo-Hygrometer 608', 'Environmental Monitoring',  'Testo'),

  -- Barometers (11,12)
  ('Digital Barometer DM-9601A',    'Environmental Monitoring',  'Lutron'),
  ('Precision Barometer PTB110',    'Measurement Instruments',   'Vaisala'),

  -- Tensile Testing Machines (13,14)
  ('Universal Testing Machine UTM-50',  'Industrial Equipment',  'Tinius Olsen'),
  ('Tensile Testing Machine TT-2000',   'Industrial Equipment',  'Zwick Roell'),

  -- Weighing Scales (15,16,17)
  ('Analytical Balance AB-204',     'Weighing Scales',           'Mettler Toledo'),
  ('Precision Scale PS-3200',       'Weighing Scales',           'Kern'),
  ('Industrial Platform Scale IPS', 'Weighing Scales',           'A&D Weighing');


-- ============================================================
-- 3. SERIAL NUMBERS (physical units)
-- ============================================================

-- pH Meters
INSERT INTO serial_numbers (product_id, serial_number, status) VALUES
  (1, 'PHM-HI98130-001', 'available'),
  (1, 'PHM-HI98130-002', 'available'),
  (1, 'PHM-HI98130-003', 'sold'),
  (2, 'PHM-HI98103-001', 'available'),
  (2, 'PHM-HI98103-002', 'available'),
  (3, 'PHM-PH500-001',   'available'),
  (3, 'PHM-PH500-002',   'damaged');

-- Thermometers
INSERT INTO serial_numbers (product_id, serial_number, status) VALUES
  (4, 'THM-TM902-001',   'available'),
  (4, 'THM-TM902-002',   'available'),
  (4, 'THM-TM902-003',   'sold'),
  (4, 'THM-TM902-004',   'sold'),
  (5, 'THM-GM300-001',   'available'),
  (5, 'THM-GM300-002',   'available'),
  (6, 'THM-RTD-001',     'available');

-- Temperature Controllers
INSERT INTO serial_numbers (product_id, serial_number, status) VALUES
  (7, 'TC-AUTNCS-001',   'available'),
  (7, 'TC-AUTNCS-002',   'available'),
  (7, 'TC-AUTNCS-003',   'sold'),
  (8, 'TC-OMEGA-001',    'available'),
  (8, 'TC-OMEGA-002',    'available');

-- Thermohygrometers
INSERT INTO serial_numbers (product_id, serial_number, status) VALUES
  (9,  'THY-HT1000-001', 'available'),
  (9,  'THY-HT1000-002', 'available'),
  (9,  'THY-HT1000-003', 'sold'),
  (10, 'THY-608-001',    'available'),
  (10, 'THY-608-002',    'available'),
  (10, 'THY-608-003',    'sold');

-- Barometers
INSERT INTO serial_numbers (product_id, serial_number, status) VALUES
  (11, 'BAR-DM9601-001', 'available'),
  (11, 'BAR-DM9601-002', 'available'),
  (12, 'BAR-PTB110-001', 'available');

-- Tensile Testing Machines
INSERT INTO serial_numbers (product_id, serial_number, status) VALUES
  (13, 'TTM-UTM50-001',  'available'),
  (13, 'TTM-UTM50-002',  'sold'),
  (14, 'TTM-TT2000-001', 'available');

-- Weighing Scales
INSERT INTO serial_numbers (product_id, serial_number, status) VALUES
  (15, 'WS-AB204-001',   'available'),
  (15, 'WS-AB204-002',   'available'),
  (15, 'WS-AB204-003',   'sold'),
  (16, 'WS-PS3200-001',  'available'),
  (16, 'WS-PS3200-002',  'available'),
  (17, 'WS-IPS-001',     'available'),
  (17, 'WS-IPS-002',     'available'),
  (17, 'WS-IPS-003',     'damaged');


-- ============================================================
-- 4. STOCK TRANSACTIONS
-- ============================================================

-- Transaction 1: Initial bulk stock in (pH meters + thermometers)
INSERT INTO stock_transactions (transaction_type, reference_note, created_at) VALUES
  ('IN', 'PO-2024-001 · Initial stock from Hanna Instruments', '2026-06-01 09:00:00');

INSERT INTO stock_transaction_items (transaction_id, product_id, serial_id, quantity)
SELECT 1, s.product_id, s.serial_id, 1
FROM serial_numbers s
WHERE s.serial_number IN ('PHM-HI98130-001','PHM-HI98130-002','PHM-HI98130-003',
                          'PHM-HI98103-001','PHM-HI98103-002',
                          'THM-TM902-001','THM-TM902-002','THM-TM902-003','THM-TM902-004');

-- Transaction 2: Stock in — Temperature controllers + barometers
INSERT INTO stock_transactions (transaction_type, reference_note, created_at) VALUES
  ('IN', 'PO-2024-002 · Autonics & Vaisala order', '2026-06-15 10:30:00');

INSERT INTO stock_transaction_items (transaction_id, product_id, serial_id, quantity)
SELECT 2, s.product_id, s.serial_id, 1
FROM serial_numbers s
WHERE s.serial_number IN ('TC-AUTNCS-001','TC-AUTNCS-002','TC-AUTNCS-003',
                          'TC-OMEGA-001','TC-OMEGA-002',
                          'BAR-DM9601-001','BAR-DM9601-002','BAR-PTB110-001');

-- Transaction 3: Stock in — Weighing scales
INSERT INTO stock_transactions (transaction_type, reference_note, created_at) VALUES
  ('IN', 'PO-2024-003 · Mettler Toledo & Kern delivery', '2026-07-01 11:00:00');

INSERT INTO stock_transaction_items (transaction_id, product_id, serial_id, quantity)
SELECT 3, s.product_id, s.serial_id, 1
FROM serial_numbers s
WHERE s.serial_number IN ('WS-AB204-001','WS-AB204-002','WS-AB204-003',
                          'WS-PS3200-001','WS-PS3200-002',
                          'WS-IPS-001','WS-IPS-002','WS-IPS-003');

-- Transaction 4: Stock in — Thermohygrometers & Tensile
INSERT INTO stock_transactions (transaction_type, reference_note, created_at) VALUES
  ('IN', 'PO-2024-004 · Testo & Zwick order', '2026-07-10 09:15:00');

INSERT INTO stock_transaction_items (transaction_id, product_id, serial_id, quantity)
SELECT 4, s.product_id, s.serial_id, 1
FROM serial_numbers s
WHERE s.serial_number IN ('THY-HT1000-001','THY-HT1000-002','THY-HT1000-003',
                          'THY-608-001','THY-608-002','THY-608-003',
                          'TTM-UTM50-001','TTM-UTM50-002','TTM-TT2000-001');

-- Transaction 5: Stock OUT — Sales to ABC Analytical Labs
INSERT INTO stock_transactions (transaction_type, reference_note, created_at) VALUES
  ('OUT', 'INV-2024-101 · Customer: ABC Analytical Labs', '2026-08-05 14:00:00');

INSERT INTO stock_transaction_items (transaction_id, product_id, serial_id, quantity)
SELECT 5, s.product_id, s.serial_id, 1
FROM serial_numbers s
WHERE s.serial_number IN ('PHM-HI98130-003','THM-TM902-003','THM-TM902-004','TC-AUTNCS-003');

-- Transaction 6: Stock OUT — Sales to XYZ Research Institute
INSERT INTO stock_transactions (transaction_type, reference_note, created_at) VALUES
  ('OUT', 'INV-2024-102 · Customer: XYZ Research Institute', '2026-08-20 16:30:00');

INSERT INTO stock_transaction_items (transaction_id, product_id, serial_id, quantity)
SELECT 6, s.product_id, s.serial_id, 1
FROM serial_numbers s
WHERE s.serial_number IN ('WS-AB204-003','THY-HT1000-003','THY-608-003','TTM-UTM50-002');

-- Transaction 7: Stock in — Remaining items (pH bench + infrared + RTD + benchtop)
INSERT INTO stock_transactions (transaction_type, reference_note, created_at) VALUES
  ('IN', 'PO-2024-005 · Mixed batch — Jenway, Lutron, Fluke', '2026-09-01 10:00:00');

INSERT INTO stock_transaction_items (transaction_id, product_id, serial_id, quantity)
SELECT 7, s.product_id, s.serial_id, 1
FROM serial_numbers s
WHERE s.serial_number IN ('PHM-PH500-001','PHM-PH500-002',
                          'THM-GM300-001','THM-GM300-002',
                          'THM-RTD-001');


-- ============================================================
-- 5. VERIFY — Summary
-- ============================================================
SELECT '=== USERS ===' AS '';
SELECT user_id, username, role FROM users;

SELECT '=== PRODUCTS (17 total) ===' AS '';
SELECT product_id, product_name, category, brand FROM products ORDER BY product_id;

SELECT '=== SERIAL NUMBERS ===' AS '';
SELECT COUNT(*) AS total_serials,
       SUM(status='available') AS available,
       SUM(status='sold') AS sold,
       SUM(status='damaged') AS damaged
FROM serial_numbers;

SELECT '=== TRANSACTIONS ===' AS '';
SELECT transaction_id, transaction_type, reference_note, created_at FROM stock_transactions;

SELECT '=== SAMPLE COMPLETE — READY FOR PRESENTATION ===' AS '';
