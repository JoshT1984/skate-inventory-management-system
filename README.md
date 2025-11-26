# 🛹 skate-inventory-management-system

A full-stack skateboard warehouse inventory system built with Spring Boot, Angular, and PostgreSQL. Supports managing products, tracking stock across multiple warehouses, enforcing capacity limits, and maintaining clean many-to-many inventory relationships.

---

## 📦 Overview

This application manages skateboard products—decks, trucks, wheels, bearings, apparel, and accessories—across multiple warehouse locations.  
Admins can add warehouses, create products, and track inventory stored in each warehouse through a searchable and intuitive interface.

The system uses a normalized many-to-many data model that links warehouses and products through a dedicated warehouse inventory table.

---

## 🧱 Features

### Warehouse Management
- Create, update, and deactivate warehouses  
- Track maximum and current capacity  
- View warehouse dashboards with inventory counts  
- Prevent adding inventory beyond capacity  

### Product Management
- Create and manage a global catalog of skateboard products  
- Store product attributes such as SKU, category, brand, and description  
- Share products across multiple warehouses  

### Warehouse Inventory
- Add products to specific warehouses with quantity and storage location  
- Update quantities and track stock levels per warehouse  
- Prevent duplicate warehouse–product entries  
- Support (optional) inventory transfer logic  

---

## 🗄️ Database Design

The system uses three core tables:

### 1. `warehouse`
Stores warehouse data such as name, location, capacity, and active status.

### 2. `product`
Represents the global product catalog (SKUs, categories, brands, and descriptions).

### 3. `warehouse_inventory`
A junction table connecting warehouses and products, containing:
- `quantity`  
- `storage_location`  
- a unique `(warehouse_id, product_id)` constraint  

This structure forms a clean many-to-many relationship between warehouses and products.

---

## 🔗 ERD

```
[ warehouse ] 1 --- ∞ [ warehouse_inventory ] ∞ --- 1 [ product ]
```

/skate-inventory-management-system
 ├── backend/
 │    ├── src/main/java/
 │    ├── src/main/resources/
 │    └── pom.xml
 │
 ├── frontend/
 │    ├── src/app/
 │    ├── angular.json
 │    └── package.json
 │
 ├── README.md
 ├── .gitignore
 └── LICENSE

 ---

## 📚 API Endpoints (Planned)

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
- `GET /warehouses/{id}/inventory`
- `POST /warehouses/{id}/inventory`
- `PUT /warehouse-inventory/{id}`
- `DELETE /warehouse-inventory/{id}`
- *(Optional)* `POST /inventory/transfer`

---

## 🧪 Future Enhancements

- Low-stock alerts  
- Warehouse capacity warnings  
- Product expiration or obsolescence tracking  
- Inventory transfer history  
- Reporting features (CSV/PDF export)  
- User authentication & role-based access  
- UI dashboard metrics and charts  

---

## 📜 License

This project is licensed under the **MIT License**.

---

## 🤝 Contributing

Pull requests are welcome.  
For significant changes, please open an issue first to discuss the proposed update.

---

## 📸 Screenshots (To Be Added)

<!-- You can later add something like: -->
<!-- ![Dashboard Screenshot](images/dashboard.png) -->
<!-- ![Warehouse View](images/warehouse-view.png) -->

---

## 🙌 Acknowledgments

- SkillStorm Full-Stack Java Program  
- Open-source Java & Angular communities  
- PostgreSQL documentation  
