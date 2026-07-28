import { Component, OnInit, inject, signal } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { DashboardService, DashboardData } from '../../services/dashboard';

@Component({
  selector: 'app-admin-dashboard',
  imports: [DecimalPipe],
  templateUrl: './admin-dashboard.html',
  styleUrl: './admin-dashboard.css',
})
export class AdminDashboard implements OnInit {
  private dashboardService = inject(DashboardService);

  dashboard = signal<DashboardData | null>(null);
  loading = signal(true);
  errorMessage = signal('');

  ngOnInit() {
    this.fetchDashboard();
  }

  fetchDashboard() {
    this.loading.set(true);
    this.errorMessage.set('');
    this.dashboardService.getDashboard().subscribe({
      next: (data) => {
        this.dashboard.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        this.errorMessage.set('Could not load dashboard data. Please check if the backend is running.');
        this.loading.set(false);
        console.error(err);
      },
    });
  }

  // Bar width is relative to the highest-selling product in the list
  getBarPercent(unitsSold: number): number {
    const products = this.dashboard()?.topProducts ?? [];
    if (products.length === 0) return 0;
    const max = Math.max(...products.map((p) => p.unitsSold));
    if (max === 0) return 0;
    return (unitsSold / max) * 100;
  }

  statusClass(status: string): string {
    return status.toLowerCase();
  }
}