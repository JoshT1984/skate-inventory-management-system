# 🛹 skate-inventory-management-system

A full-stack skateboard warehouse inventory system built with Spring Boot, Angular, and PostgreSQL. Supports managing products, tracking stock across multiple warehouses, enforcing capacity limits, and maintaining clean many-to-many inventory relationships.

---

## 📦 Overview

This application manages skateboard products—decks, trucks, wheels, bearings, safety gear, and accessories—across multiple warehouse locations.  
Admins can add warehouses, create products, and track inventory stored in each warehouse through a clean, responsive, and searchable interface.

The system uses a normalized many-to-many data model that links warehouses and products through a dedicated warehouse inventory table, ensuring accurate capacity tracking and real-time stock visibility.

---

## 🧱 Features

### Warehouse Management
- Create, update, and deactivate warehouses  
- Track maximum and current capacity  
- Automatic updates to capacity when inventory is added, updated, moved, or deleted  
- Prevent adding inventory beyond warehouse limits  
- Clean UI with validation, success alerts, and search filtering  

### Product Management
- Create and manage a global catalog of skateboard products  
- Store product attributes such as SKU, category, brand, and description  
- Prevent duplicate SKUs  
- Share global products across all warehouses  

### Warehouse Inventory
- Add products to specific warehouses with quantity and storage location  
- Update quantities, move items between warehouses, or delete entries  
- Built-in protection against duplicate warehouse–product pairs  
- Transfer inventory with automatic capacity adjustments  
- Search and filter warehouse inventory quickly  

---

## 🗄️ Database Design

The system uses three core tables:

### 1. `warehouses`
Stores warehouse metadata including name, location, capacity limits, and active status.

### 2. `products`
Represents the global product catalog with SKUs, categories, brands, and detailed descriptions.

### 3. `warehouse_inventories`
A junction table connecting warehouses and products, containing:
- `quantity`  
- `storage_location`  
- A unique `(warehouse_id, product_id)` constraint  
- Enforced referential integrity with foreign keys  

This structure forms a clean many-to-many relationship between warehouses and products while enabling scalable inventory management.

---

## 🔗 ERD

[ warehouse ] 1 --- ∞ [ warehouse_inventory ] ∞ --- 1 [ product ]


Project structure:

/skate-inventory-management-system
├── backend/
│ ├── src/main/java/
│ ├── src/main/resources/
│ └── pom.xml
│
├── frontend/
│ ├── src/app/
│ ├── angular.json
│ └── package.json
│
├── README.md
├── .gitignore
└── LICENSE


---

## 📚 API Endpoints (Implemented & Planned)

### 🏢 Warehouse
- `GET /warehouses`
- `GET /warehouses/{id}`
- `POST /warehouses`
- `PUT /warehouses/{id}`
- `DELETE /warehouses/{id}`

### 📦 Product
- `GET /products`
- `POST /products`
- `PUT /products/{id}`
- `DELETE /products/{id}`

### 🔄 Warehouse Inventory
- `GET /warehouse-inventory`
- `POST /warehouse-inventory`
- `PUT /warehouse-inventory/{id}`
- `DELETE /warehouse-inventory/{id}`
- `POST /warehouse-inventory/transfer` *(Optional Advanced Feature)*

---

## 🧪 Future Enhancements

- Low-stock alerts and dashboard notifications  
- Warehouse capacity warnings with visual indicators  
- Inventory transfer logs and audit history  
- Reporting features (CSV/PDF export)  
- Product expiration or obsolescence tracking  
- Role-based authentication (Admin/User)  
- Enhanced analytic dashboards and charts  

---

## 📜 License

This project is licensed under the **MIT License**.

---

## 🤝 Contributing

Pull requests are welcome.  
For significant changes, please open an issue first to discuss the proposed update.

---

## 📸 Screenshots (To Be Added)

<!-- Add screenshots when UI is finalized -->
<!-- ![Dashboard Screenshot](images/dashboard.png) -->
<!-- ![Warehouse View](images/warehouse-view.png) -->

---

## 🙌 Acknowledgments

- SkillStorm Full-Stack Java Developer Program  
- Open-source Java, Spring, and Angular communities  
- PostgreSQL and pgAdmin tools  
