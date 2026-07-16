import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-contact',
  imports: [FormsModule],
  templateUrl: './contact.html',
  styleUrl: './contact.css',
})
export class Contact {
  name = '';
  email = '';
  message = '';
  submitted = false;

  onSubmit() {
    if (!this.name || !this.email || !this.message) {
      return;
    }
    console.log('Contact form submitted:', {
      name: this.name,
      email: this.email,
      message: this.message,
    });
    this.submitted = true;
    this.name = '';
    this.email = '';
    this.message = '';
  }
}