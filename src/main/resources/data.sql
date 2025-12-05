-- Warehouses
INSERT INTO WAREHOUSE (
    warehouse_id, name, location, address, max_capacity, current_capacity, code, is_active, created_at, updated_at
) VALUES
(1, 'Central Distribution Center', 'Austin, TX', '123 Main St, Austin, TX', 10000, 4500, 'WHS-AUS-001', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'North Texas Storage Hub', 'Dallas, TX', '890 Warehouse Rd, Dallas, TX', 15000, 6000, 'WHS-DAL-002', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'South Regional Depot', 'San Antonio, TX', '45 River Bend Dr, San Antonio, TX', 8000, 3000, 'WHS-SAT-003', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Products
INSERT INTO PRODUCT (
    product_id, name, sku, category, brand, description
) VALUES
(1, 'Bluetooth Speaker', 'SKU-1001', 'Electronics', 'SoundMax', 'Portable wireless speaker with high quality audio'),
(2, 'LED Monitor 27in', 'SKU-1002', 'Electronics', 'ViewPro', 'Full HD monitor with adjustable stand'),
(3, 'Running Shoes', 'SKU-1003', 'Footwear', 'FleetFeet', 'Lightweight running shoes for daily training'),
(4, 'Backpack 30L', 'SKU-1004', 'Outdoor', 'TrailPack', 'Durable hiking backpack with multiple compartments'),
(5, 'Water Bottle 1L', 'SKU-1005', 'Outdoor', 'HydroPure', 'Stainless steel insulated water bottle');

-- Warehouse Inventory (Join Table)
INSERT INTO WAREHOUSE_INVENTORY (
    warehouse_inventory_id, warehouse_id, product_id, quantity, storage_location
) VALUES
(1, 1, 1, 120, 'Aisle 1 - Shelf 2'),
(2, 1, 2, 75, 'Aisle 2 - Shelf 1'),
(3, 1, 4, 210, 'Aisle 5 - Shelf 3'),

(4, 2, 1, 300, 'Zone B - Bin 12'),
(5, 2, 3, 90, 'Zone A - Rack 4'),
(6, 2, 5, 500, 'Zone C - Bin 3'),

(7, 3, 3, 60, 'Row 3 - Slot 7'),
(8, 3, 5, 200, 'Row 1 - Slot 2');
