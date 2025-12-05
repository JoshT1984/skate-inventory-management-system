drop table if exists WAREHOUSE_INVENTORY;
drop table if exists PRODUCT;
drop table if exists WAREHOUSE;

CREATE TABLE WAREHOUSE (
    warehouse_id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    max_capacity INTEGER NOT NULL,
    current_capacity INTEGER NOT NULL,
    code VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE PRODUCT (
    product_id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    sku VARCHAR(255) NOT NULL,
    category VARCHAR(255) NOT NULL,
    brand VARCHAR(255) NOT NULL,
    description TEXT NOT NULL
);

CREATE TABLE WAREHOUSE_INVENTORY (
    warehouse_inventory_id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    warehouse_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    storage_location VARCHAR(255) NOT NULL,

-- foreign keys
CONSTRAINT fk_warehouse_inventory_warehouse
    FOREIGN KEY (warehouse_id) REFERENCES WAREHOUSE (warehouse_id),
CONSTRAINT fk_warehouse_inventory_product
    FOREIGN KEY (product_id) REFERENCES PRODUCT (product_id),

-- many-to-many uniqueness: one row per warehouse/product pair
CONSTRAINT uq_warehouse_inventory_wh_prod
    UNIQUE (warehouse_id, product_id)

);