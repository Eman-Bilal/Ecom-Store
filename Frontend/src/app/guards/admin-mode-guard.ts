import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AppModeService } from '../services/app-mode';

export const adminModeGuard: CanActivateFn = () => {
  const appMode = inject(AppModeService);
  const router = inject(Router);

  if (appMode.mode() === 'admin') {
    return true;
  }

  router.navigate(['/']);
  return false;
};