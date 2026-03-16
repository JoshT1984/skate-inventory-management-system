TRUNCATE TABLE warehouse_inventories RESTART IDENTITY CASCADE;
TRUNCATE TABLE warehouses RESTART IDENTITY CASCADE;
TRUNCATE TABLE products RESTART IDENTITY CASCADE;
TRUNCATE TABLE oauth_identities RESTART IDENTITY CASCADE;
TRUNCATE TABLE user_roles RESTART IDENTITY CASCADE;
TRUNCATE TABLE roles RESTART IDENTITY CASCADE;
TRUNCATE TABLE users RESTART IDENTITY CASCADE;

INSERT INTO roles (name) VALUES ('GUEST'), ('EMPLOYEE'), ('MANAGER'), ('ADMIN') ON CONFLICT (name) DO NOTHING;

INSERT INTO users (first_name, last_name, email, status) VALUES
('Joshua', 'Admin', 'joshuathompson0526@gmail.com', 'ACTIVE'),
('Karen', 'Manager', 'karenthompson8707@gmail.com', 'ACTIVE'),
('Demo', 'Employee', 'warehouse.employee@example.com', 'ACTIVE'),
('Demo', 'Guest', 'shop.guest@example.com', 'ACTIVE');

INSERT INTO user_roles (user_id, role_id)
SELECT 1, role_id FROM roles WHERE name = 'ADMIN'
UNION ALL
SELECT 2, role_id FROM roles WHERE name = 'MANAGER'
UNION ALL
SELECT 3, role_id FROM roles WHERE name = 'EMPLOYEE'
UNION ALL
SELECT 4, role_id FROM roles WHERE name = 'GUEST';

INSERT INTO products (name, sku, category, brand, description) VALUES
('Street Deck 8.0" Pro Model', 'SKU-0001-DECK80', 'Deck', 'Baker', '8.0 inch 7-ply maple deck for technical street skating.'),
('Street Deck 8.25" Team Graphic', 'SKU-0002-DECK825', 'Deck', 'Element', '8.25 inch all-around deck for street and park.'),
('Stage 11 Trucks 5.0" Raw', 'SKU-0005-TRUCK50', 'Trucks', 'Independent', 'Pair of 5.0 inch trucks ideal for 7.75–8.0 inch decks.'),
('Formula Four Wheels 52mm 99a', 'SKU-0008-WHEEL52', 'Wheels', 'Spitfire', 'Set of four 52 mm 99a wheels for technical street.'),
('Bones Reds Bearings', 'SKU-0011-BEAR-REDS', 'Bearings', 'Bones', 'Set of eight Bones Reds bearings with spacers and washers.'),
('Mob Grip 9" x 33"', 'SKU-0013-GRIP-MOB', 'Griptape', 'Mob Grip', 'Standard black grip tape sheet with perforations for easy application.'),
('Classic Certified Helmet', 'SKU-0017-HELM-PRO', 'Safety', 'Pro-Tec', 'Certified skate helmet with adjustable fit system.'),
('Drop-Through 40" Longboard Deck', 'SKU-0020-LBDECK40', 'Longboard', 'Landyachtz', '40 inch drop-through longboard deck for commuting and carving.');

INSERT INTO warehouses (name, location, address, max_capacity, current_capacity, code, is_active) VALUES
('Central Texas HQ', 'Austin, TX', '1001 Congress Warehouse Blvd, Austin, TX 78701', 6000, 1140, 'CTX-HQ', TRUE),
('Gulf Coast Distribution Center', 'Houston, TX', '2450 Bayou Logistics Park, Houston, TX 77002', 5000, 620, 'HOU-GULF', TRUE),
('DFW Street Supply Hub', 'Dallas, TX', '780 Skatepark Dr, Dallas, TX 75201', 5000, 515, 'DFW-STREET', TRUE);

INSERT INTO warehouse_inventories (warehouse_id, product_id, quantity, storage_location) VALUES
(1, 1, 180, 'A1-Decks-Row1'),
(1, 3, 220, 'B1-Trucks-Low'),
(1, 4, 260, 'C1-Wheels-Street'),
(1, 5, 300, 'D1-Bearings-Bin3'),
(1, 6, 180, 'E1-Grip-TopShelf'),
(2, 1, 90, 'A1-Decks-Front'),
(2, 4, 200, 'C1-Wheels-Park'),
(2, 7, 120, 'F1-Helmets-Rack2'),
(2, 8, 210, 'G1-Longboard-Decks'),
(3, 2, 140, 'A1-Decks-Street'),
(3, 5, 150, 'D1-Reds-Case'),
(3, 6, 225, 'B1-Grip-Core');
