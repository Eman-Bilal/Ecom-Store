import { Component, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AppModeService } from '../../services/app-mode';

@Component({
  selector: 'app-customer-layout',
  imports: [RouterLink, RouterLinkActive, RouterOutlet, FormsModule],
  templateUrl: './customer-layout.html',
  styleUrl: './customer-layout.css',
})
export class CustomerLayout {
  cartCount = 2;
  appMode = inject(AppModeService);
  router = inject(Router);

  showAdminPrompt = false;
  adminEmail = '';
  adminPassword = '';
  adminError = '';

  openAdminLogin() {
    this.showAdminPrompt = !this.showAdminPrompt;
    this.adminError = '';
  }

  submitAdminLogin() {
    const success = this.appMode.switchToAdmin(this.adminEmail, this.adminPassword);
    if (success) {
      this.showAdminPrompt = false;
      this.adminEmail = '';
      this.adminPassword = '';
      this.router.navigate(['/admin/products']);
    } else {
      this.adminError = 'Invalid admin credentials.';
    }
  }

  goHome() {
    this.router.navigate(['/']);
  }
}