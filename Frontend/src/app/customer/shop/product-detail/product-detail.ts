import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DecimalPipe } from '@angular/common';
import { ProductService, ProductResponse, getProductImageSrc } from '../../../services/product';
import { CartService } from '../../../services/cart';

@Component({
  selector: 'app-product-detail',
  imports: [DecimalPipe],
  templateUrl: './product-detail.html',
  styleUrl: './product-detail.css',
})
export class ProductDetail implements OnInit {
  private route = inject(ActivatedRoute);
  private productService = inject(ProductService);
  private cartService = inject(CartService);

  productId = this.route.snapshot.params['id'];

  product = signal<ProductResponse | null>(null);
  loading = signal(true);
  errorMessage = signal('');
  quantity = signal(1);
  addedMessage = signal('');

  ngOnInit() {
    this.productService.getById(this.productId).subscribe({
      next: (data) => {
        this.product.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        this.errorMessage.set('Could not load this product.');
        this.loading.set(false);
        console.error(err);
      },
    });
  }

  imageSrc(product: ProductResponse): string | null {
    return getProductImageSrc(product);
  }

  increment() {
    const p = this.product();
    if (!p) return;
    if (this.quantity() < p.quantityInStock) {
      this.quantity.update((q) => q + 1);
    }
  }

  decrement() {
    if (this.quantity() > 1) {
      this.quantity.update((q) => q - 1);
    }
  }

  addToCart() {
    const p = this.product();
    if (!p || p.quantityInStock === 0) return;
    this.cartService.addToCart(p, this.quantity());
    this.addedMessage.set('Added to cart!');
    setTimeout(() => this.addedMessage.set(''), 2000);
  }
}