import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DecimalPipe, DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { CartService } from '../../services/cart';
import { OrderService, CreateOrderRequest, OrderInvoiceDto } from '../../services/order';

@Component({
  selector: 'app-checkout',
  imports: [FormsModule, DecimalPipe, DatePipe, RouterLink],
  templateUrl: './checkout.html',
  styleUrl: './checkout.css',
})
export class Checkout {
  private cartService = inject(CartService);
  private orderService = inject(OrderService);

  cartItems = this.cartService.cartItems;
  shipping = 500;

  firstName = '';
  lastName = '';
  email = '';
  phone = '';
  shippingAddress = '';
  city = '';
  postalCode = '';

  submitting = signal(false);
  errorMessage = signal('');
  placedInvoice = signal<OrderInvoiceDto | null>(null);

  get subtotal(): number {
    return this.cartService.totalPrice();
  }

  get total(): number {
    return this.subtotal + (this.cartItems().length ? this.shipping : 0);
  }

  placeOrder() {
    if (this.cartItems().length === 0) {
      this.errorMessage.set('Your cart is empty.');
      return;
    }

    if (!this.firstName || !this.lastName || !this.email || !this.phone || !this.shippingAddress || !this.city) {
      this.errorMessage.set('Please fill in all required fields.');
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

    this.submitting.set(true);
    this.errorMessage.set('');

    this.orderService.createOrder(request).subscribe({
      next: (invoice) => {
        this.submitting.set(false);
        this.cartService.clearCart();
        this.placedInvoice.set(invoice);
        this.resetForm();
      },
      error: (err) => {
        this.submitting.set(false);
        this.errorMessage.set(err?.error?.message || 'Could not place order. Please check your details.');
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