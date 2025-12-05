CREATE TABLE "warehouse"(
    "warehouse_id" INTEGER NOT NULL,
    "name" VARCHAR(255) NOT NULL,
    "location" VARCHAR(255) NOT NULL,
    "address" VARCHAR(255) NOT NULL,
    "max_capacity" INTEGER NOT NULL,
    "current_capacity" INTEGER NOT NULL,
    "code" VARCHAR(255) NOT NULL,
    "is_active" BOOLEAN NOT NULL,
    "created_at" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    "updated_at" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL
);
ALTER TABLE
    "warehouse" ADD PRIMARY KEY("warehouse_id");
CREATE TABLE "warehouse_inventory"(
    "warehouse_inventory_id" INTEGER NOT NULL,
    "warehouse_id" INTEGER NOT NULL,
    "product_id" INTEGER NOT NULL,
    "quantity" INTEGER NOT NULL,
    "storage_location" VARCHAR(255) NOT NULL
);
ALTER TABLE
    "warehouse_inventory" ADD PRIMARY KEY("warehouse_inventory_id");
ALTER TABLE
    "warehouse_inventory" ADD CONSTRAINT "warehouse_inventory_warehouse_id_unique" UNIQUE("warehouse_id");
ALTER TABLE
    "warehouse_inventory" ADD CONSTRAINT "warehouse_inventory_product_id_unique" UNIQUE("product_id");
CREATE TABLE "product"(
    "product_id" INTEGER NOT NULL,
    "name" VARCHAR(255) NOT NULL,
    "sku" VARCHAR(255) NOT NULL,
    "catgory" VARCHAR(255) NOT NULL,
    "brand" VARCHAR(255) NOT NULL,
    "description" TEXT NOT NULL
);
ALTER TABLE
    "product" ADD PRIMARY KEY("product_id");
ALTER TABLE
    "warehouse_inventory" ADD CONSTRAINT "warehouse_inventory_warehouse_id_foreign" FOREIGN KEY("warehouse_id") REFERENCES "warehouse"("warehouse_id");
ALTER TABLE
    "warehouse_inventory" ADD CONSTRAINT "warehouse_inventory_product_id_foreign" FOREIGN KEY("product_id") REFERENCES "product"("product_id");