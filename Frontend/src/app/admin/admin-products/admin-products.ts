import { Component } from '@angular/core';

interface AdminProduct {
  id: number;
  name: string;
  category: string;
  price: string;
  stock: number;
}

@Component({
  selector: 'app-admin-products',
  templateUrl: './admin-products.html',
  styleUrl: './admin-products.css',
})
export class AdminProducts {
  products: AdminProduct[] = [
    { id: 1, name: 'Meridian Classic Automatic', category: "Men's — Automatic", price: 'Rs. 24,500', stock: 12 },
    { id: 2, name: 'Oakridge Chronograph', category: "Men's — Chronograph", price: 'Rs. 32,900', stock: 3 },
    { id: 3, name: 'Aria Mesh Watch', category: "Women's — Mesh", price: 'Rs. 19,800', stock: 0 },
  ];

  stockLabel(stock: number): string {
    if (stock === 0) return 'Out of stock';
    if (stock <= 5) return 'Low stock';
    return 'In stock';
  }

  stockClass(stock: number): string {
    if (stock === 0) return 'out';
    if (stock <= 5) return 'low';
    return 'ok';
  }

  deleteProduct(id: number) {
    this.products = this.products.filter((p) => p.id !== id);
  }
}