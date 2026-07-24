import { Component, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AppModeService } from '../../services/app-mode';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-admin-layout',
  imports: [RouterLink, RouterLinkActive, RouterOutlet],
  templateUrl: './admin-layout.html',
  styleUrl: './admin-layout.css',
})
export class AdminLayout {
  appMode = inject(AppModeService);
  authService = inject(AuthService);
  router = inject(Router);

  exitAdmin() {
    this.authService.logout();
    this.appMode.switchToCustomer();
    this.router.navigate(['/']);
  }
}