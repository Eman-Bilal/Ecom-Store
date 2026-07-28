import { Component, OnInit, inject, signal } from '@angular/core';
import { DecimalPipe, DatePipe } from '@angular/common';
import { OrderService, CustomerOrder, OrderStatus } from '../../services/order';

@Component({
  selector: 'app-admin-orders',
  imports: [DecimalPipe, DatePipe],
  templateUrl: './admin-order-status.html',
  styleUrl: './admin-order-status.css',
})
export class AdminOrders implements OnInit {
  private orderService = inject(OrderService);

  orders = signal<CustomerOrder[]>([]);
  loading = signal(true);
  errorMessage = signal('');
  updatingOrderId = signal<string | null>(null);

  statusOptions: OrderStatus[] = ['PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED'];

  ngOnInit() {
    this.fetchOrders();
  }

  fetchOrders() {
    this.loading.set(true);
    this.errorMessage.set('');
    this.orderService.getAllOrders().subscribe({
      next: (data) => {
        this.orders.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        this.errorMessage.set('Could not load orders. Please check if the backend is running.');
        this.loading.set(false);
        console.error(err);
      },
    });
  }
 
  onStatusChange(order: CustomerOrder, event: Event) {
    const newStatus = (event.target as HTMLSelectElement).value as OrderStatus;
    if (newStatus === order.orderStatus) return;

    this.updatingOrderId.set(order.id);

    this.orderService.updateStatus(order.id, newStatus).subscribe({
      next: (updated) => {
        this.updatingOrderId.set(null);
        this.orders.update((list) =>
          list.map((o) => (o.id === updated.id ? updated : o))
        );
      },
      error: (err) => {
        this.updatingOrderId.set(null);
        alert('Could not update order status.');
        console.error(err);
      },
    });
  }

  statusClass(status: string): string {
    return status.toLowerCase();
  }
}