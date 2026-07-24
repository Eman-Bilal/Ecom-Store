import { Injectable, signal } from '@angular/core';

export type AppMode = 'customer' | 'admin';

@Injectable({ providedIn: 'root' })
export class AppModeService {
  mode = signal<AppMode>('customer');

  setAdminMode() {
    this.mode.set('admin');
  }

  switchToCustomer() {
    this.mode.set('customer');
  }
}