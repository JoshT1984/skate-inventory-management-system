DROP TABLE IF EXISTS WAREHOUSE_INVENTORY;
DROP TABLE IF EXISTS PRODUCT;
DROP TABLE IF EXISTS WAREHOUSE;

CREATE TABLE WAREHOUSE (
    warehouse_id SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(100) NOT NULL,
    address VARCHAR(200) NOT NULL,
    max_capacity INTEGER NOT NULL,
    current_capacity INTEGER NOT NULL,
    code VARCHAR(20) NOT NULL,
    is_active BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT uq_warehouse_code UNIQUE (code),
    CONSTRAINT uq_warehouse_name_location UNIQUE (name, location)
);

CREATE TABLE PRODUCT (
    product_id SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(100) NOT NULL,
    sku VARCHAR(40) NOT NULL,
    category VARCHAR(50) NOT NULL,
    brand VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    CONSTRAINT uq_product_sku UNIQUE (sku)
);

CREATE TABLE WAREHOUSE_INVENTORY (
    warehouse_inventory_id SERIAL PRIMARY KEY NOT NULL,
    warehouse_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    storage_location VARCHAR(50) NOT NULL,
-- foreign keys
CONSTRAINT fk_warehouse_inventory_warehouse
    FOREIGN KEY (warehouse_id) REFERENCES WAREHOUSE (warehouse_id),
CONSTRAINT fk_warehouse_inventory_product
    FOREIGN KEY (product_id) REFERENCES PRODUCT (product_id),
-- many-to-many uniqueness: one row per warehouse/product pair
CONSTRAINT uq_warehouse_inventory_wh_prod
    UNIQUE (warehouse_id, product_id)
);