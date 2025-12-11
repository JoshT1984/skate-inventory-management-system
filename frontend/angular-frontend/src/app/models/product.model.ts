export class Product {
  productId: number;
  name: string;
  sku: string;
  category: string;
  brand: string;
  description: string;

  constructor(
    productId: number,
    name: string,
    sku: string,
    category: string,
    brand: string,
    description: string
  ) {
    this.productId = productId;
    this.name = name;
    this.sku = sku;
    this.category = category;
    this.brand = brand;
    this.description = description;
  }
}
