import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';

interface Product {
  id: number;
  name: string;
  category: string;
  price: string;
  imageUrl: string;
}

@Component({
  selector: 'app-home',
  imports: [RouterLink],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {
  newArrivals: Product[] = [
    { id: 1, name: 'Meridian Classic Automatic', category: "Men's — Automatic", price: 'Rs. 24,500' , imageUrl:'watch2.jpeg' },
    { id: 2, name: 'Oakridge Chronograph', category: "Men's — Chronograph", price: 'Rs. 32,900' , imageUrl:'watch3.jpeg' },
    { id: 3, name: 'Aria Mesh Watch', category: "Women's — Mesh", price: 'Rs. 19,800' , imageUrl:'watch4.jpeg' },
    { id: 4, name: 'Terra Connect Smartwatch', category: 'Smart Watch', price: 'Rs. 28,200' , imageUrl:'watch5.jpeg' },
  ];
}