import { Component, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AppModeService } from '../../services/app-mode';
import { AuthService } from '../../services/auth';
import { NgClass } from '@angular/common';

@Component({
  selector: 'app-customer-layout',
  imports: [RouterLink, RouterLinkActive, RouterOutlet, FormsModule , NgClass],
  templateUrl: './customer-layout.html',
  styleUrl: './customer-layout.css',
})
export class CustomerLayout {
  cartCount = 2;
  appMode = inject(AppModeService);
  authService = inject(AuthService);
  router = inject(Router);

  showAdminPrompt = false;
  adminEmail = '';
  adminPassword = '';
  adminError = '';
  loggingIn = false;
  showPassword = false;

  openAdminLogin() {
    this.showAdminPrompt = !this.showAdminPrompt;
    this.adminError = '';
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  submitAdminLogin() {
    if (!this.adminEmail || !this.adminPassword) {
      this.adminError = 'Email and password are required.';
      return;
    }

    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailPattern.test(this.adminEmail)) {
      this.adminError = 'Please enter a valid email address.';
      return;
    }

    this.loggingIn = true;
    this.adminError = '';

    this.authService.login(this.adminEmail, this.adminPassword).subscribe({
      next: (res) => {
        localStorage.setItem('admin_token', res.token);
        this.loggingIn = false;
        this.showAdminPrompt = false;
        this.adminEmail = '';
        this.adminPassword = '';
        this.appMode.setAdminMode();
        this.router.navigate(['/admin/dashboard']);
      },
      error: (err) => {
        this.loggingIn = false;
        this.adminError = 'Invalid email or password.';
        console.error(err);
      },
    });
  }

  goHome() {
    this.router.navigate(['/']);
  }
}