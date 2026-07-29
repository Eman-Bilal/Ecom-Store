import { Injectable, signal, computed } from '@angular/core';
import { Product } from './product';

export interface CartItem {
  product: Product;
  quantity: number;
}

@Injectable({ providedIn: 'root' })
export class CartService {
  private items = signal<CartItem[]>([]);

  cartItems = this.items.asReadonly();

  totalCount = computed(() =>
    this.items().reduce((sum, item) => sum + item.quantity, 0)
  );

  totalPrice = computed(() =>
    this.items().reduce((sum, item) => sum + item.product.price * item.quantity, 0)
  );

  addToCart(product: Product, quantity: number) {
    this.items.update((list) => {
      const existing = list.find((i) => i.product.id === product.id);
      if (existing) {
        return list.map((i) =>
          i.product.id === product.id
            ? { ...i, quantity: Math.min(i.quantity + quantity, product.quantityInStock) }
            : i
        );
      }
      return [...list, { product, quantity }];
    });
  }

  removeFromCart(productId: string) {
    this.items.update((list) => list.filter((i) => i.product.id !== productId));
  }

  updateQuantity(productId: string, quantity: number) {
    this.items.update((list) =>
      list.map((i) =>
        i.product.id === productId
          ? { ...i, quantity: Math.max(1, Math.min(quantity, i.product.quantityInStock)) }
          : i
      )
    );
  }

  clearCart() {
    this.items.set([]);
  }
}