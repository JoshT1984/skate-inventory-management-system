-- Warehouses
INSERT INTO WAREHOUSE (
    name, location, address, max_capacity, current_capacity, code, is_active, created_at, updated_at
) VALUES
('Central Distribution Center', 'Austin, TX', '123 Main St, Austin, TX', 10000, 4500, 'WHS-AUS-001', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('North Texas Storage Hub', 'Dallas, TX', '890 Warehouse Rd, Dallas, TX', 15000, 6000, 'WHS-DAL-002', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('South Regional Depot', 'San Antonio, TX', '45 River Bend Dr, San Antonio, TX', 8000, 3000, 'WHS-SAT-003', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Products
INSERT INTO PRODUCT (
    name, sku, category, brand, description
) VALUES
('Bluetooth Speaker', 'SKU-1001', 'Electronics', 'SoundMax', 'Portable wireless speaker with high quality audio'),
('LED Monitor 27in', 'SKU-1002', 'Electronics', 'ViewPro', 'Full HD monitor with adjustable stand'),
('Running Shoes', 'SKU-1003', 'Footwear', 'FleetFeet', 'Lightweight running shoes for daily training'),
('Backpack 30L', 'SKU-1004', 'Outdoor', 'TrailPack', 'Durable hiking backpack with multiple compartments'),
('Water Bottle 1L', 'SKU-1005', 'Outdoor', 'HydroPure', 'Stainless steel insulated water bottle');

-- Warehouse Inventory (Join Table)
-- Note: we keep warehouse_id and product_id because they are FOREIGN KEYS, 
-- but we drop warehouse_inventory_id so it can auto-increment.
INSERT INTO WAREHOUSE_INVENTORY (
    warehouse_id, product_id, quantity, storage_location
) VALUES
(1, 1, 120, 'Aisle 1 - Shelf 2'),
(1, 2, 75, 'Aisle 2 - Shelf 1'),
(1, 4, 210, 'Aisle 5 - Shelf 3'),

(2, 1, 300, 'Zone B - Bin 12'),
(2, 3, 90, 'Zone A - Rack 4'),
(2, 5, 500, 'Zone C - Bin 3'),

(3, 3, 60, 'Row 3 - Slot 7'),
(3, 5, 200, 'Row 1 - Slot 2');
