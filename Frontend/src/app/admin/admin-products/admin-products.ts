import { Component, OnInit, inject } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { ProductService, Product } from '../../services/product';

@Component({
  selector: 'app-admin-products',
  imports: [DecimalPipe],
  templateUrl: './admin-products.html',
  styleUrl: './admin-products.css',
})
export class AdminProducts implements OnInit {
  private productService = inject(ProductService);

  products: Product[] = [];
  loading:boolean = true;
  errorMessage = '';

  ngOnInit() {
    this.fetchProducts();
  }

  fetchProducts() {
    this.loading = true;
    this.errorMessage = '';
    this.productService.getAllProducts().subscribe({
      next: (data) => {
        console.log('RAW API RESPONSE:', data); // temporary debug line
        this.products = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('API ERROR:', err); // temporary debug line
        this.errorMessage = 'Could not load products. Please check if the backend is running.';
        this.loading = false;
      },
    });
  }

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

  deleteProduct(id: string) {
    this.productService.deleteProduct(id).subscribe({
      next: () => {
        this.products = this.products.filter((p) => p.id !== id);
      },
      error: (err) => {
        console.error(err);
        alert('Could not delete the product.');
      },
    });
  }
}