import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ContactService } from '../../services/contact';

@Component({
  selector: 'app-contact',
  imports: [FormsModule],
  templateUrl: './contact.html',
  styleUrl: './contact.css',
})
export class Contact {
  private contactService = inject(ContactService);

  name = '';
  email = '';
  subject = '';
  message = '';

  submitting = signal(false);
  errorMessage = signal('');
  showToast = signal(false);

  onSubmit() {
    if (!this.name || !this.email || !this.subject || !this.message) {
      this.errorMessage.set('Please fill in all fields.');
      return;
    }

    this.submitting.set(true);
    this.errorMessage.set('');

    this.contactService
      .submit({
        name: this.name,
        email: this.email,
        subject: this.subject,
        message: this.message,
      })
      .subscribe({
        next: () => {
          this.submitting.set(false);
          this.showToast.set(true);
          this.resetFields();

          setTimeout(() => {
            this.showToast.set(false);
          }, 3000);
        },
        error: (err) => {
          this.submitting.set(false);
          this.errorMessage.set('Could not send your message. Please try again.');
          console.error(err);
        },
      });
  }

  resetFields() {
    this.name = '';
    this.email = '';
    this.subject = '';
    this.message = '';
  }
}