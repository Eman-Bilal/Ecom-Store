import { Injectable, signal } from '@angular/core';

export interface ToastMessage {
  text: string;
  type: 'success' | 'error';
}

@Injectable({ providedIn: 'root' })
export class ToastService {
  message = signal<ToastMessage | null>(null);
  private timeoutId: any;

  show(text: string, type: 'success' | 'error' = 'success') {
    clearTimeout(this.timeoutId);
    this.message.set({ text, type });
    this.timeoutId = setTimeout(() => this.message.set(null), 3000);
  }
}