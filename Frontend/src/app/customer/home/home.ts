import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DecimalPipe } from '@angular/common';
import { ProductService, Product } from '../../services/product';

@Component({
  selector: 'app-home',
  imports: [RouterLink, DecimalPipe],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {
  private productService = inject(ProductService);

  newArrivals = signal<Product[]>([]);
  loading = signal(true);
  errorMessage = signal('');

  ngOnInit() {
    this.productService.getAllProducts().subscribe({
      next: (data) => {
        // Only show active products; most recently added first, capped at 4
        const active = data.filter((p) => p.active);
        this.newArrivals.set(active.slice(-4).reverse());
        this.loading.set(false);
      },
      error: (err) => {
        this.errorMessage.set('Could not load new arrivals.');
        this.loading.set(false);
        console.error(err);
      },
    });
  }
}