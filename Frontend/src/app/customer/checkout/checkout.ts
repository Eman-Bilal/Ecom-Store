import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { DecimalPipe } from '@angular/common';
import { CartService } from '../../services/cart';
import { OrderService, CreateOrderRequest } from '../../services/order';

@Component({
  selector: 'app-checkout',
  imports: [FormsModule, DecimalPipe],
  templateUrl: './checkout.html',
  styleUrl: './checkout.css',
})
export class Checkout {
  private cartService = inject(CartService);
  private orderService = inject(OrderService);
  private router = inject(Router);

  cartItems = this.cartService.cartItems;
  shipping = 500;

  firstName = '';
  lastName = '';
  email = '';
  phone = '';
  shippingAddress = '';
  city = '';
  postalCode = '';

  submitting = false;
  errorMessage = '';

  get subtotal(): number {
    return this.cartService.totalPrice();
  }

  get total(): number {
    return this.subtotal + (this.cartItems().length ? this.shipping : 0);
  }

placeOrder() {
  if (this.cartItems().length === 0) {
    this.errorMessage = 'Your cart is empty.';
    return;
  }

  if (!this.firstName || !this.lastName || !this.email || !this.phone || !this.shippingAddress || !this.city) {
    this.errorMessage = 'Please fill in all required fields.';
    return;
  }

  const request: CreateOrderRequest = {
    firstName: this.firstName,
    lastName: this.lastName,
    email: this.email,
    phone: this.phone,
    shippingAddress: this.shippingAddress,
    city: this.city,
    postalCode: this.postalCode || undefined,
    items: this.cartItems().map((item) => ({
      productId: item.product.id,
      quantity: item.quantity,
    })),
  };

  this.submitting = true;
  this.errorMessage = '';

  this.orderService.createOrder(request).subscribe({
    next: (order) => {
      this.submitting = false;
      this.cartService.clearCart();
      this.resetForm();
      this.router.navigate(['/order-confirmation'], { state: { order } });
    },
    error: (err) => {
      this.submitting = false;
      this.errorMessage = err?.error?.message || 'Could not place order. Please check your details.';
      console.error(err);
    },
  });
}

private resetForm() {
  this.firstName = '';
  this.lastName = '';
  this.email = '';
  this.phone = '';
  this.shippingAddress = '';
  this.city = '';
  this.postalCode = '';
}
}