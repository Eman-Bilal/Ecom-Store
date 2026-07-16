import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-checkout',
  imports: [FormsModule],
  templateUrl: './checkout.html',
  styleUrl: './checkout.css',
})
export class Checkout {
  fullName = '';
  address = '';
  city = '';
  postalCode = '';

  placeOrder() {
    console.log('Order placed:', { fullName: this.fullName, address: this.address, city: this.city, postalCode: this.postalCode });
  }
}