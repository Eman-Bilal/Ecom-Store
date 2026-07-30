import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DecimalPipe, DatePipe } from '@angular/common';
import { inject } from '@angular/core';
import { OrderService, CustomerOrder } from '../../services/order';

@Component({
  selector: 'app-track-order',
  imports: [FormsModule, DecimalPipe, DatePipe],
  templateUrl: './track-order.html',
  styleUrl: './track-order.css',
})
export class TrackOrder {
  private orderService = inject(OrderService);

  trackOrderNumber = '';
  trackEmail = '';
  trackResult = signal<CustomerOrder | null>(null);
  trackError = signal('');
  tracking = signal(false);

  trackOrder() {
    if (!this.trackOrderNumber || !this.trackEmail) {
      this.trackError.set('Please enter both order number and email.');
      return;
    }

    this.tracking.set(true);
    this.trackError.set('');
    this.trackResult.set(null);

    this.orderService.trackOrder(this.trackOrderNumber, this.trackEmail).subscribe({
      next: (order) => {
        this.tracking.set(false);
        this.trackResult.set(order);
      },
      error: (err) => {
        this.tracking.set(false);
        this.trackError.set('Order not found. Please check the order number and email.');
        console.error(err);
      },
    });
  }
}