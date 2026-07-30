import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environments';

export type OrderStatus = 'PENDING' | 'PROCESSING' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED';

export interface OrderItemDetailDto {
  productName: string;
  quantity: number;
  unitPrice: number;
  lineTotal: number;
}

export interface OrderInvoiceDto {
  orderNumber: string;
  customerName: string;
  email: string;
  phone: string;
  shippingAddress: string;
  city: string;
  postalCode: string;
  orderStatus: OrderStatus;
  items: OrderItemDetailDto[];
  subtotal: number;
  shippingFee: number;
  totalAmount: number;
  placedAt: string;
}

export interface CustomerOrder {
  id: string;
  orderNumber: string;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  city: string;
  postalCode: string;
  shippingAddress: string;
  shippingFee: number;
  subtotal: number;
  totalAmount: number;
  orderStatus: OrderStatus;
  createdAt: string;
}

export interface OrderItemRequest {
  productId: string;
  quantity: number;
}

export interface CreateOrderRequest {
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  shippingAddress: string;
  city: string;
  postalCode?: string;
  items: OrderItemRequest[];
}

@Injectable({ providedIn: 'root' })
export class OrderService {
  private baseUrl = `${environment.apiUrl}/orders`;

  constructor(private http: HttpClient) {}

  getAllOrders(): Observable<CustomerOrder[]> {
    return this.http.get<CustomerOrder[]>(this.baseUrl);
  }

  updateStatus(id: string, status: OrderStatus): Observable<CustomerOrder> {
    const params = new HttpParams().set('status', status);
    return this.http.patch<CustomerOrder>(`${this.baseUrl}/${id}/status`, null, { params });
  }

  createOrder(request: CreateOrderRequest): Observable<OrderInvoiceDto> {
    return this.http.post<OrderInvoiceDto>(`${this.baseUrl}/checkout`, request);
  }

  trackOrder(orderNumber: string, email: string): Observable<CustomerOrder> {
    const params = new HttpParams().set('orderNumber', orderNumber).set('email', email);
    return this.http.get<CustomerOrder>(`${this.baseUrl}/track`, { params });
  }
  getInvoiceByOrderNumberAndEmail(orderNumber: string, email: string): Observable<OrderInvoiceDto> {
  const params = new HttpParams().set('orderNumber', orderNumber).set('email', email);
  return this.http.get<OrderInvoiceDto>(`${this.baseUrl}/track/invoice`, { params });
}
}