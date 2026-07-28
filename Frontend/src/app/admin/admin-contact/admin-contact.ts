import { Component, OnInit, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ContactService, ContactMessage } from '../../services/contact';

@Component({
  selector: 'app-admin-contact',
  imports: [DatePipe],
  templateUrl: './admin-contact.html',
  styleUrl: './admin-contact.css',
})
export class AdminContact implements OnInit {
  private contactService = inject(ContactService);

  messages = signal<ContactMessage[]>([]);
  loading = signal(true);
  errorMessage = signal('');
  resolvingId = signal<string | null>(null);
  expandedId = signal<string | null>(null);

  ngOnInit() {
    this.fetchMessages();
  }

  fetchMessages() {
    this.loading.set(true);
    this.errorMessage.set('');
    this.contactService.getAll().subscribe({
      next: (data) => {
        this.messages.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        this.errorMessage.set('Could not load messages. Please check if the backend is running.');
        this.loading.set(false);
        console.error(err);
      },
    });
  }

  toggleExpand(id: string) {
    this.expandedId.set(this.expandedId() === id ? null : id);
  }

  markResolved(id: string) {
    this.resolvingId.set(id);
    this.contactService.markResolved(id).subscribe({
      next: (updated) => {
        this.resolvingId.set(null);
        this.messages.update((list) =>
          list.map((m) => (m.id === updated.id ? updated : m))
        );
      },
      error: (err) => {
        this.resolvingId.set(null);
        alert('Could not mark message as resolved.');
        console.error(err);
      },
    });
  }
}