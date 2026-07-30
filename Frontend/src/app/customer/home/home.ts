import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DecimalPipe } from '@angular/common';
import { ProductService, ProductResponse, getProductImageSrc } from '../../services/product';

@Component({
  selector: 'app-home',
  imports: [RouterLink, DecimalPipe],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {
  private productService = inject(ProductService);

  newArrivals = signal<ProductResponse[]>([]);
  loading = signal(true);
  errorMessage = signal('');

  ngOnInit() {
    this.productService.getAllProducts().subscribe({
      next: (data) => {
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

  imageSrc(product: ProductResponse): string | null {
    return getProductImageSrc(product);
  }
}