import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductService, Product } from '../../services/product';

@Component({
  selector: 'app-shop',
  imports: [RouterLink, DecimalPipe, FormsModule],
  templateUrl: './shop.html',
  styleUrl: './shop.css',
})
export class Shop implements OnInit {
  private productService = inject(ProductService);

  products = signal<Product[]>([]);
  loading = signal(true);
  errorMessage = signal('');
  searchName = '';

  ngOnInit() {
    this.fetchProducts();
  }

  fetchProducts() {
    this.loading.set(true);
    this.errorMessage.set('');
    this.productService.getAllProducts().subscribe({
      next: (data) => {
        this.products.set(data.filter((p) => p.active));
        this.loading.set(false);
      },
      error: (err) => {
        this.errorMessage.set('Could not load products. Please check if the backend is running.');
        this.loading.set(false);
        console.error(err);
      },
    });
  }

  onSearch() {
    if (!this.searchName.trim()) {
      this.fetchProducts();
      return;
    }
    this.loading.set(true);
    this.productService.searchProducts({ name: this.searchName.trim() }).subscribe({
      next: (data) => {
        this.products.set(data.filter((p) => p.active));
        this.loading.set(false);
      },
      error: (err) => {
        this.errorMessage.set('Search failed. Please try again.');
        this.loading.set(false);
        console.error(err);
      },
    });
  }
}