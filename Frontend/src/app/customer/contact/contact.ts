import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ContactService } from '../../services/contact';
import { ToastService } from '../../services/toast';

@Component({
  selector: 'app-contact',
  imports: [FormsModule],
  templateUrl: './contact.html',
  styleUrl: './contact.css',
})
export class Contact {
  private contactService = inject(ContactService);
  private toastService = inject(ToastService);

  name = '';
  email = '';
  subject = '';
  message = '';

  submitting = signal(false);
  errorMessage = signal('');

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
          this.toastService.show('Message sent successfully!', 'success');
          this.name = '';
          this.email = '';
          this.subject = '';
          this.message = '';
        },
        error: (err) => {
          this.submitting.set(false);
          this.errorMessage.set('Could not send your message. Please try again.');
          console.error(err);
        },
      });
  }
}