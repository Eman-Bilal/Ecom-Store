import { Component, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.html',
  styleUrl: './product-detail.css',
})
export class ProductDetail {
  route = inject(ActivatedRoute);
  productId = Number(this.route.snapshot.params['id']);
  quantity = 1;

  increment() { this.quantity++; }
  decrement() { if (this.quantity > 1) this.quantity--; }
}