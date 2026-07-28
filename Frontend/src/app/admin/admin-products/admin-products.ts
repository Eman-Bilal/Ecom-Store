import { Component, OnInit, inject, signal } from '@angular/core';
import { DecimalPipe, SlicePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductService, Product, ProductFormData } from '../../services/product';
import { CategoryService, Category } from '../../services/category';

@Component({
  selector: 'app-admin-products',
  imports: [DecimalPipe, SlicePipe, FormsModule],
  templateUrl: './admin-products.html',
  styleUrl: './admin-products.css',
})
export class AdminProducts implements OnInit {
  private productService = inject(ProductService);
  private categoryService = inject(CategoryService);

  // Render-critical state -> signals, so the view always updates reliably
  products = signal<Product[]>([]);
  categories = signal<Category[]>([]);
  loading = signal(true);
  errorMessage = signal('');
  showForm = signal(false);
  submitting = signal(false);
  formError = signal('');
  editingProduct = signal<Product | null>(null);

  // Plain fields for ngModel-bound inputs (typing works fine either way)
  searchName = '';
  formName = '';
  formDescription = '';
  formPrice: number | null = null;
  formQuantityInStock: number | null = null;
  formCategoryId: number | null = null;

  ngOnInit() {
    this.fetchProducts();
    this.fetchCategories();
  }

  fetchProducts() {
    this.loading.set(true);
    this.errorMessage.set('');
    this.productService.getAllProducts().subscribe({
      next: (data) => {
        this.products.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        this.errorMessage.set('Could not load products. Please check if the backend is running.');
        this.loading.set(false);
        console.error(err);
      },
    });
  }

  fetchCategories() {
    this.categoryService.getAllCategories().subscribe({
      next: (data) => {
        this.categories.set(data);
      },
      error: (err) => console.error('Could not load categories:', err),
    });
  }

  onSearch() {
    if (!this.searchName.trim()) {
      this.fetchProducts();
      return;
    }
    this.loading.set(true);
    this.productService.searchProducts({ name: this.searchName.trim() }).subscribe({
      next: (data) => {
        this.products.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        this.errorMessage.set('Search failed. Please try again.');
        this.loading.set(false);
        console.error(err);
      },
    });
  }

  stockLabel(stock: number): string {
    if (stock === 0) return 'Out of stock';
    if (stock <= 5) return 'Low stock';
    return 'In stock';
  }

  stockClass(stock: number): string {
    if (stock === 0) return 'out';
    if (stock <= 5) return 'low';
    return 'ok';
  }

  openAddForm() {
    this.editingProduct.set(null);
    this.formName = '';
    this.formDescription = '';
    this.formPrice = null;
    this.formQuantityInStock = null;
    const cats = this.categories();
    this.formCategoryId = cats.length ? cats[0].id : null;
    this.formError.set('');
    this.showForm.set(true);
  }

  openEditForm(product: Product) {
    this.editingProduct.set(product);
    this.formName = product.name;
    this.formDescription = product.description;
    this.formPrice = product.price;
    this.formQuantityInStock = product.quantityInStock;
    this.formCategoryId = product.category?.id ?? null;
    this.formError.set('');
    this.showForm.set(true);
  }

  closeForm() {
    this.showForm.set(false);
  }

  submitForm() {
    if (!this.formName || !this.formDescription || this.formPrice == null || this.formQuantityInStock == null || this.formCategoryId == null) {
      this.formError.set('Please fill in all fields.');
      return;
    }

    this.submitting.set(true);
    this.formError.set('');

    const currentlyEditing = this.editingProduct();

    const payload: ProductFormData = {
      name: this.formName,
      description: this.formDescription,
      price: this.formPrice,
      quantityInStock: this.formQuantityInStock,
      active: currentlyEditing ? currentlyEditing.active : true,
    };

    if (currentlyEditing) {
      this.productService.updateProduct(currentlyEditing.id, this.formCategoryId, payload).subscribe({
        next: (updated) => {
          this.submitting.set(false);
          this.showForm.set(false);
          this.products.update((list) => list.map((p) => (p.id === updated.id ? updated : p)));
        },
        error: (err) => {
          this.submitting.set(false);
          this.formError.set('Could not update product. Please check the fields.');
          console.error(err);
        },
      });
    } else {
      this.productService.createProduct(this.formCategoryId, payload).subscribe({
        next: (created) => {
          this.submitting.set(false);
          this.showForm.set(false);
          this.products.update((list) => [...list, created]);
        },
        error: (err) => {
          this.submitting.set(false);
          this.formError.set('Could not create product. Please check the fields.');
          console.error(err);
        },
      });
    }
  }

  deleteProduct(id: string) {
    this.productService.deleteProduct(id).subscribe({
      next: () => {
        this.products.update((list) => list.filter((p) => p.id !== id));
      },
      error: (err) => {
        console.error(err);
        alert('Could not delete the product.');
      },
    });
  }
}