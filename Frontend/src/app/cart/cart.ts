import { Component } from '@angular/core';
import { RouterLink} from '@angular/router';
import { DecimalPipe } from '@angular/common';

interface CartLine {
  id: number;
  name: string;
  price: number;
  quantity: number;
}

@Component({
  selector: 'app-cart',
  imports: [RouterLink , DecimalPipe],
  templateUrl: './cart.html',
  styleUrl: './cart.css',
})
export class Cart {
  items: CartLine[] = [
    { id: 1, name: 'Meridian Classic Automatic', price: 24500, quantity: 1 },
    { id: 2, name: 'Steel Mesh Strap', price: 4200, quantity: 1 },
  ];
  shipping = 500;

  remove(id: number) {
    this.items = this.items.filter((item) => item.id !== id);
  }

  get subtotal(): number {
    return this.items.reduce((sum, item) => sum + item.price * item.quantity, 0);
  }

  get total(): number {
    return this.subtotal + this.shipping;
  }
}