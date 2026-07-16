import { Component } from '@angular/core';
import { DecimalPipe } from '@angular/common';

interface TopProduct {
  name: string;
  units: number;
  percent: number;
}

interface RecentOrder {
  id: string;
  customer: string;
  total: number;
  status: 'delivered' | 'shipped' | 'pending';
}

@Component({
  selector: 'app-admin-dashboard',
  imports: [DecimalPipe],
  templateUrl: './admin-dashboard.html',
  styleUrl: './admin-dashboard.css',
})
export class AdminDashboard {
  revenue = 184200;
  ordersThisWeek = 46;
  lowStockCount = 3;
  totalProducts = 86;

  topProducts: TopProduct[] = [
    { name: 'Meridian Classic Automatic', units: 32, percent: 92 },
    { name: 'Oakridge Chronograph', units: 27, percent: 78 },
    { name: 'Aria Mesh Watch', units: 21, percent: 60 },
    { name: 'Steel Mesh Strap', units: 14, percent: 40 },
  ];

  recentOrders: RecentOrder[] = [
    { id: '#1042', customer: 'Ali Khan', total: 24500, status: 'delivered' },
    { id: '#1041', customer: 'Sara Ahmed', total: 4200, status: 'shipped' },
    { id: '#1040', customer: 'Bilal Iqbal', total: 32900, status: 'pending' },
    { id: '#1039', customer: 'Hina Malik', total: 19800, status: 'delivered' },
  ];
}