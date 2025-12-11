-- PRODUCTS
INSERT INTO products (name, sku, category, brand, description) VALUES
('Street Skateboard Deck 8.0in',  'SKU-0001-DECK80', 'Deck',      'ThunderSkate',
 '8.0 inch maple skateboard deck for technical street and park skating.'),
('Street Skateboard Deck 8.25in', 'SKU-0002-DECK825', 'Deck',     'ThunderSkate',
 '8.25 inch maple skateboard deck for all around street and transition skating.'),
('Low Skateboard Trucks 5.25in',  'SKU-0003-TRUCK525', 'Trucks',  'GrindKing',
 'Pair of low profile 5.25 inch trucks designed for 8.0 inch decks.'),
('Mid Skateboard Trucks 5.5in',   'SKU-0004-TRUCK55',  'Trucks',  'GrindKing',
 'Pair of mid height 5.5 inch trucks suitable for 8.25–8.5 inch decks.'),
('Skateboard Wheels 52mm 99a',    'SKU-0005-WHEEL52',  'Wheels',  'RollFast',
 'Set of four 52 mm 99a urethane wheels for street skating.'),
('Skateboard Wheels 54mm 101a',   'SKU-0006-WHEEL54',  'Wheels',  'RollFast',
 'Set of four 54 mm 101a urethane wheels for park and transition skating.'),
('ABEC 7 Bearings Set',           'SKU-0007-BEAR7',    'Bearings','SwiftSpin',
 'Set of eight ABEC 7 precision bearings with spacers included.'),
('Complete Skate Tool',           'SKU-0008-TOOL',     'Tools',   'AllInOne',
 'Multi function skate tool for trucks, wheels, and hardware adjustments.'),
('Classic Skate Helmet',          'SKU-0009-HELMET',   'Safety',  'SafeRide',
 'Certified skate helmet with adjustable straps and low profile fit.'),
('Knee and Elbow Pad Set',        'SKU-0010-PADS',     'Safety',  'SafeRide',
 'Set of knee and elbow pads with hard caps for impact protection.');

-- WAREHOUSES
INSERT INTO warehouses
(name, location, address, max_capacity, current_capacity, code, is_active, created_at, updated_at)
VALUES
('Central Texas Main Warehouse',
 'Killeen, TX',
 '1234 Warehouse Way, Killeen, TX 76542',
 10000, 4200,
 'CTX-MAIN',
 TRUE,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('North Distribution Hub',
 'Dallas, TX',
 '5678 Distribution Dr, Dallas, TX 75201',
 8000, 3100,
 'DAL-HUB',
 TRUE,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('South Regional Storage',
 'Austin, TX',
 '9101 Regional Rd, Austin, TX 78701',
 5000, 1200,
 'AUS-REG',
 TRUE,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- WAREHOUSE_INVENTORIES
-- Assume product_ids start at 1 in the order inserted above
-- and warehouse_ids start at 1 in the order inserted above.

INSERT INTO warehouse_inventories
(warehouse_id, product_id, quantity, storage_location)
VALUES
-- Central Texas Main Warehouse (id 1)
(1, 1, 150, 'A1-Decks-Top'),
(1, 2, 120, 'A1-Decks-Mid'),
(1, 3, 200, 'B2-Trucks-Low'),
(1, 5, 300, 'C3-Wheels-Row1'),
(1, 7, 400, 'D1-Bearings-Bin4'),
(1, 9, 75,  'E1-Helmets-Shelf2'),

-- North Distribution Hub (id 2)
(2, 1, 80,  'A2-Decks-Front'),
(2, 4, 140, 'B1-Trucks-Row3'),
(2, 6, 220, 'C2-Wheels-Row2'),
(2, 8, 90,  'D2-Tools-Bin1'),
(2, 10, 60, 'E2-Pads-Shelf1'),

-- South Regional Storage (id 3)
(3, 2, 60,  'A3-Decks-Back'),
(3, 5, 110, 'C4-Wheels-Row1'),
(3, 7, 150, 'D3-Bearings-Bin2'),
(3, 9, 40,  'E3-Helmets-Shelf3');
