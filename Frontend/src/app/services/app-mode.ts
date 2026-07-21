import { Injectable, signal } from '@angular/core';

export type AppMode = 'customer' | 'admin';

@Injectable({ providedIn: 'root' })
export class AppModeService {
  mode = signal<AppMode>('customer');

  private adminCredentials = [
    { email: 'rameeshashafiq73@gmail.com', password: 'Admin@123' },
  ];

  verifyAdmin(email: string, password: string): boolean {
    return this.adminCredentials.some((c) => c.email === email && c.password === password);
  }

  switchToAdmin(email: string, password: string): boolean {
    if (this.verifyAdmin(email, password)) {
      this.mode.set('admin');
      return true;
    }
    return false;
  }

  switchToCustomer() {
    this.mode.set('customer');
  }
}