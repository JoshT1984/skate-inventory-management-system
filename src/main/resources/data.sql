------------------------------------------------------------
-- OPTIONAL: wipe existing data (order matters: child -> parent)
------------------------------------------------------------
TRUNCATE TABLE warehouse_inventories RESTART IDENTITY CASCADE;
TRUNCATE TABLE warehouses RESTART IDENTITY CASCADE;
TRUNCATE TABLE products RESTART IDENTITY CASCADE;

------------------------------------------------------------
-- PRODUCTS
------------------------------------------------------------
INSERT INTO products (name, sku, category, brand, description) VALUES
('Street Deck 8.0" Pro Model',       'SKU-0001-DECK80',   'Deck',      'Baker',
 '8.0 inch 7-ply maple deck for technical street skating.'),
('Street Deck 8.25" Team Graphic',   'SKU-0002-DECK825',  'Deck',      'Element',
 '8.25 inch all-around deck for street and park.'),
('Park Deck 8.5" Powell Classic',    'SKU-0003-DECK85',   'Deck',      'Powell Peralta',
 '8.5 inch deck with medium concave for bowls and transition.'),
('Cruiser Deck 9.0" Screaming Hand', 'SKU-0004-DECK90',   'Deck',      'Santa Cruz',
 '9.0 inch cruiser deck with old school shape and mellow concave.'),

('Stage 11 Trucks 5.0" Raw',         'SKU-0005-TRUCK50',  'Trucks',    'Independent',
 'Pair of 5.0 inch trucks ideal for 7.75–8.0 inch decks.'),
('Lights II Trucks 5.25" Hollow',    'SKU-0006-TRUCK525', 'Trucks',    'Thunder',
 '5.25 inch hollow kingpin trucks for responsive turning.'),
('Polished Trucks 5.8" High',        'SKU-0007-TRUCK58',  'Trucks',    'Venture',
 '5.8 inch high trucks suited for 8.5 inch decks and up.'),

('Formula Four Wheels 52mm 99a',     'SKU-0008-WHEEL52',  'Wheels',    'Spitfire',
 'Set of four 52 mm 99a wheels for technical street.'),
('Street Tech Wheels 54mm 101a',     'SKU-0009-WHEEL54',  'Wheels',    'Bones',
 'Set of four 54 mm 101a wheels for park and ledges.'),
('Clouds Wheels 56mm 92a',           'SKU-0010-WHEEL56',  'Wheels',    'Ricta',
 'Set of four 56 mm 92a soft wheels for rough streets.'),

('Bones Reds Bearings',              'SKU-0011-BEAR-REDS','Bearings',  'Bones',
 'Set of eight Bones Reds bearings with spacers and washers.'),
('Bronson G3 Bearings',              'SKU-0012-BEAR-G3',  'Bearings',  'Bronson',
 'Set of eight Bronson G3 high-speed bearings.'),

('Mob Grip 9" x 33"',                'SKU-0013-GRIP-MOB', 'Griptape',  'Mob Grip',
 'Standard black grip tape sheet with perforations for easy application.'),
('Hardware 1" Allen Set (Black)',    'SKU-0014-HARD1-AL', 'Hardware',  'Shorty''s',
 'Set of eight 1 inch Allen hardware bolts with nuts.'),
('Hardware 1.25" Phillips Set',      'SKU-0015-HARD125-P','Hardware',  'Shorty''s',
 'Set of eight 1.25 inch Phillips hardware bolts with nuts.'),

('All-In-One Skate Tool',            'SKU-0016-TOOL-AIO', 'Tools',     'Independent',
 'Compact skate tool with sockets for trucks, wheels, and hardware.'),
('Classic Certified Helmet',         'SKU-0017-HELM-PRO', 'Safety',    'Pro-Tec',
 'Certified skate helmet with adjustable fit system.'),
('Street Knee/Elbow Pad Combo',      'SKU-0018-PADS-SET', 'Safety',    'Triple Eight',
 'Knee and elbow pad combo for street and park skating.'),
('Slide Gloves with Pucks',          'SKU-0019-GLOVES',   'Safety',    'Sector 9',
 'Longboard slide gloves with removable pucks.'),

('Drop-Through 40" Longboard Deck',  'SKU-0020-LBDECK40', 'Longboard', 'Landyachtz',
 '40 inch drop-through longboard deck for commuting and carving.'),
('Paris V3 180mm Trucks',            'SKU-0021-LBTRUCK',  'Longboard', 'Paris',
 'Pair of 180 mm longboard trucks for carving and cruising.'),
('70mm 78a Longboard Wheels',        'SKU-0022-LBWHEEL70','Longboard', 'Sector 9',
 'Set of four 70 mm 78a soft longboard wheels for smooth rides.'),

('Riser Pads 1/8"',                  'SKU-0023-RISER18',  'Accessories','Independent',
 'Set of two 1/8 inch riser pads to reduce wheelbite.'),
('Medium Bushings Set',              'SKU-0024-BUSH-MED', 'Accessories','Bones',
 'Set of medium durometer bushings for standard trucks.');

------------------------------------------------------------
-- WAREHOUSES
------------------------------------------------------------
INSERT INTO warehouses
(name, location, address, max_capacity, current_capacity, code, is_active, created_at, updated_at)
VALUES
-- 1
('Central Texas HQ',
 'Austin, TX',
 '1001 Congress Warehouse Blvd, Austin, TX 78701',
 6000, 1800,
 'CTX-HQ',
 TRUE,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 2
('Gulf Coast Distribution Center',
 'Houston, TX',
 '2450 Bayou Logistics Park, Houston, TX 77002',
 5000, 1050,
 'HOU-GULF',
 TRUE,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 3
('DFW Street Supply Hub',
 'Dallas, TX',
 '780 Skatepark Dr, Dallas, TX 75201',
 5000, 1280,
 'DFW-STREET',
 TRUE,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 4
('West Coast Partner Warehouse',
 'Los Angeles, CA',
 '3600 Sunset Ramp Rd, Los Angeles, CA 90026',
 7000, 1470,
 'LA-WEST',
 TRUE,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 5
('Midwest Regional Storage',
 'Kansas City, MO',
 '515 Heartland Freight Ave, Kansas City, MO 64106',
 4000, 1100,
 'KC-MID',
 TRUE,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 6
('SoCal Demo & Outlet',
 'San Diego, CA',
 '220 Ocean Front Logistics Ln, San Diego, CA 92101',
 2500, 2310,
 'SD-DEMO',
 TRUE,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 7
('Northeast Online Fulfillment',
 'New York, NY',
 '77 Manhattan Pier Warehouse, New York, NY 10001',
 3000, 2850,
 'NY-ONLINE',
 TRUE,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 8
('Rocky Mountain Pop-Up Storage',
 'Denver, CO',
 '909 Switchback Supply Rd, Denver, CO 80202',
 1500, 1480,
 'DEN-POP',
 TRUE,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

------------------------------------------------------------
-- WAREHOUSE INVENTORIES
-- (assumes product_id 1–24 and warehouse_id 1–8 in insert order)
------------------------------------------------------------

-- Central Texas HQ (warehouse_id = 1, total 1800)
INSERT INTO warehouse_inventories (warehouse_id, product_id, quantity, storage_location) VALUES
(1,  1, 180, 'A1-Decks-Row1'),
(1,  2, 160, 'A1-Decks-Row2'),
(1,  3, 120, 'A2-Park-Decks'),
(1,  5, 220, 'B1-Trucks-Low'),
(1,  8, 260, 'C1-Wheels-Street'),
(1, 11, 300, 'D1-Bearings-Bin3'),
(1, 13, 400, 'E1-Grip-TopShelf'),
(1, 17,  90, 'F1-Helmets-Rack2'),
(1, 18,  70, 'F2-Pads-Rack1');

-- Gulf Coast Distribution Center (warehouse_id = 2, total 1050)
INSERT INTO warehouse_inventories (warehouse_id, product_id, quantity, storage_location) VALUES
(2,  1,  90, 'A1-Decks-Front'),
(2,  4,  80, 'A2-Cruisers-Back'),
(2,  6, 150, 'B1-Trucks-Hollow'),
(2,  9, 200, 'C1-Wheels-Park'),
(2, 12, 220, 'D1-Bearings-G3'),
(2, 14, 180, 'E1-Hardware-1in'),
(2, 16,  60, 'F1-Tools-PegWall'),
(2, 19,  70, 'G1-Gloves-Totes');

-- DFW Street Supply Hub (warehouse_id = 3, total 1280)
INSERT INTO warehouse_inventories (warehouse_id, product_id, quantity, storage_location) VALUES
(3,  2, 140, 'A1-Decks-Street'),
(3,  3, 130, 'A2-Decks-Park'),
(3,  7, 160, 'B1-Trucks-High'),
(3,  8, 190, 'C1-Wheels-52mm'),
(3, 10, 210, 'C2-Wheels-Soft'),
(3, 11, 150, 'D1-Reds-Case'),
(3, 15, 180, 'E1-Hardware-125'),
(3, 23, 120, 'F1-Risers-Box2');

-- West Coast Partner Warehouse (warehouse_id = 4, total 1470)
INSERT INTO warehouse_inventories (warehouse_id, product_id, quantity, storage_location) VALUES
(4,  4, 130, 'A1-Cruiser-Decks'),
(4,  5, 200, 'B1-Trucks-Stage11'),
(4,  6, 220, 'B2-Trucks-Lights'),
(4,  9, 240, 'C1-Wheels-STF'),
(4, 10, 260, 'C2-Wheels-Ricta'),
(4, 20,  80, 'D1-Longboard-Decks'),
(4, 21,  90, 'D2-LB-Trucks'),
(4, 22, 110, 'D3-LB-Wheels'),
(4, 24, 140, 'E1-Bushings-Med');

-- Midwest Regional Storage (warehouse_id = 5, total 1100)
INSERT INTO warehouse_inventories (warehouse_id, product_id, quantity, storage_location) VALUES
(5,  3,  90, 'A1-Decks-Reserve'),
(5, 13, 200, 'B1-Grip-Core'),
(5, 14, 210, 'C1-Hardware-Top'),
(5, 15, 190, 'C2-Hardware-Bulk'),
(5, 18,  60, 'D1-Pads-Overflow'),
(5, 19,  80, 'D2-Gloves-Box3'),
(5, 23, 140, 'E1-Risers-Mid'),
(5, 24, 130, 'E2-Bushings-Rack1');

-- SoCal Demo & Outlet (warehouse_id = 6, total 2310, near max 2500)
INSERT INTO warehouse_inventories (warehouse_id, product_id, quantity, storage_location) VALUES
(6,  1, 350, 'A1-Pro-Completes'),
(6,  8, 330, 'B1-Street-Wheels'),
(6, 11, 320, 'C1-Bearings-Prime'),
(6, 13, 300, 'D1-Grip-Featured'),
(6, 16, 280, 'E1-Tools-Demo'),
(6, 17, 260, 'F1-Helmets-Demo'),
(6, 18, 240, 'F2-Pads-Demo'),
(6, 20, 230, 'G1-Longboards-Demo');

-- Northeast Online Fulfillment (warehouse_id = 7, total 2850, near max 3000)
INSERT INTO warehouse_inventories (warehouse_id, product_id, quantity, storage_location) VALUES
(7,  2, 380, 'A1-Decks-TopPicks'),
(7,  3, 360, 'A2-Decks-Bowls'),
(7,  4, 340, 'A3-Cruisers'),
(7,  5, 320, 'B1-Trucks-Stage11'),
(7,  6, 300, 'B2-Trucks-Lights'),
(7,  7, 280, 'B3-Trucks-High'),
(7,  9, 260, 'C1-Wheels-STF'),
(7, 10, 240, 'C2-Wheels-Cruise'),
(7, 12, 220, 'D1-Bearings-G3'),
(7, 21, 150, 'E1-LB-Trucks-Online');

-- Rocky Mountain Pop-Up Storage (warehouse_id = 8, total 1480, near max 1500)
INSERT INTO warehouse_inventories (warehouse_id, product_id, quantity, storage_location) VALUES
(8,  1, 220, 'A1-Decks-Temp'),
(8,  8, 210, 'B1-Wheels-Street-Temp'),
(8, 10, 200, 'B2-Wheels-Soft-Temp'),
(8, 14, 190, 'C1-Hardware-1in-Temp'),
(8, 15, 180, 'C2-Hardware-125-Temp'),
(8, 19, 170, 'D1-Gloves-PopUp'),
(8, 22, 160, 'E1-LB-Wheels-PopUp'),
(8, 23, 150, 'F1-Risers-PopUp');
