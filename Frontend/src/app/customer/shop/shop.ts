import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

interface Product {
  id: number;
  name: string;
  category: string;
  price: string;
}

@Component({
  selector: 'app-shop',
  imports: [RouterLink],
  templateUrl: './shop.html',
  styleUrl: './shop.css',
})
export class Shop {
  products: Product[] = [
    { id: 1, name: 'Meridian Classic Automatic', category: "Men's — Automatic", price: 'Rs. 24,500' },
    { id: 2, name: 'Oakridge Chronograph', category: "Men's — Chronograph", price: 'Rs. 32,900' },
    { id: 3, name: 'Aria Mesh Watch', category: "Women's — Mesh", price: 'Rs. 19,800' },
    { id: 4, name: 'Lumen Dive Watch', category: "Men's — Dive", price: 'Rs. 26,700' },
    { id: 5, name: 'Aurora Ladies Watch', category: "Women's — Dress", price: 'Rs. 21,300' },
    { id: 6, name: 'Terra Connect Smartwatch', category: 'Smart Watch', price: 'Rs. 28,200' },
  ];
}