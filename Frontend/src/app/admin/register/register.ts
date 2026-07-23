import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../../services/admin';

@Component({
  selector: 'app-register-admin',
  imports: [FormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  private adminService = inject(AdminService);

  firstName = '';
  lastName = '';
  email = '';
  password = '';
  phone = '';

  registrationComplete = false;
  errorMessage = '';
  submitting = false;

  onSubmit() {
    this.errorMessage = '';
    this.submitting = true;

    this.adminService
      .register({
        firstName: this.firstName,
        lastName: this.lastName,
        email: this.email,
        password: this.password,
        phone: this.phone,
      })
      .subscribe({
        next: () => {
          this.submitting = false;
          this.registrationComplete = true;
        },
        error: (err) => {
          this.submitting = false;
          this.errorMessage = err.error?.message || 'Registration has failed check fields.';
          console.error(err);
        },
      });
  }

  registerAnother() {
    this.registrationComplete = false;
    this.firstName = '';
    this.lastName = '';
    this.email = '';
    this.password = '';
    this.phone = '';
  }
}