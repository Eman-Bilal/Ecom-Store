import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DecimalPipe } from '@angular/common';
import { CartService, CartItem } from '../../services/cart';

@Component({
  selector: 'app-cart',
  imports: [RouterLink, DecimalPipe],
  templateUrl: './cart.html',
  styleUrl: './cart.css',
})
export class Cart {
  private cartService = inject(CartService);

  items = this.cartService.cartItems;
  shipping = 500;

  remove(productId: string) {
    this.cartService.removeFromCart(productId);
  }

  increment(item: CartItem) {
    if (item.quantity < item.product.quantityInStock) {
      this.cartService.updateQuantity(item.product.id, item.quantity + 1);
    }
  }

  decrement(item: CartItem) {
    if (item.quantity > 1) {
      this.cartService.updateQuantity(item.product.id, item.quantity - 1);
    }
  }

  get subtotal(): number {
    return this.cartService.totalPrice();
  }

  get total(): number {
    return this.subtotal + (this.items().length ? this.shipping : 0);
  }
}