import { Component, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AppModeService } from '../../services/app-mode';

@Component({
  selector: 'app-admin-layout',
  imports: [RouterLink, RouterLinkActive, RouterOutlet],
  templateUrl: './admin-layout.html',
  styleUrl: './admin-layout.css',
})
export class AdminLayout {
  appMode = inject(AppModeService);
  router = inject(Router);

  exitAdmin() {
    this.appMode.switchToCustomer();
    this.router.navigate(['/']);
  }
}