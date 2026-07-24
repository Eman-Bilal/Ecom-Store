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

  emailError = '';
  passwordError = '';
  errorMessage = '';
  submitting = false;
  showToast = false;

  isValidEmail(email: string): boolean {
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailPattern.test(email);
  }

  isValidPassword(password: string): string {
    if (password.length < 8) {
      return 'Password must be atleast 8 characters long.';
    }

    let hasUpperCase = false;
    let hasLowerCase = false;
    let hasNumber = false;
    let hasSpecialChar = false;
    const specialChars = '!@#$%^&*()_+-=[]{};\':"\\|,.<>/?';

    for (let i = 0; i < password.length; i++) {
      const char = password[i];

      if (char >= 'A' && char <= 'Z') {
        hasUpperCase = true;
      }
      if (char >= 'a' && char <= 'z') {
        hasLowerCase = true;
      }
      if (char >= '0' && char <= '9') {
        hasNumber = true;
      }
      if (specialChars.includes(char)) {
        hasSpecialChar = true;
      }
    }

    if (!hasUpperCase) {
      return 'Password must contain atleast one capital letter.';
    }
    if (!hasLowerCase) {
      return 'Password must contain atleast one lowercase letter.';
    }
    if (!hasNumber) {
      return 'Password must contain atleast one number.';
    }
    if (!hasSpecialChar) {
      return 'Password must contain atleast one special character.';
    }

    return '';
  }

  onSubmit() {
    this.errorMessage = '';
    this.emailError = '';
    this.passwordError = '';

    if (!this.isValidEmail(this.email)) {
      this.emailError = 'Please enter a valid email address.';
    }

    const passwordCheck = this.isValidPassword(this.password);
    if (passwordCheck) {
      this.passwordError = passwordCheck;
    }

    if (this.emailError || this.passwordError) {
      return;
    }

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
          this.showToast = true;
          this.resetFields();

          setTimeout(() => {
            this.showToast = false;
          }, 3000);
        },
        error: (err) => {
          this.submitting = false;
          this.errorMessage = err.error?.message || 'Registration has failed check fields.';
          console.error(err);
        },
      });
  }

  resetFields() {
    this.firstName = '';
    this.lastName = '';
    this.email = '';
    this.password = '';
    this.phone = '';
  }
}