import { Component, OnInit, inject } from '@angular/core';
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

  products: Product[] = [];
  categories: Category[] = [];
  loading : boolean = true;
  errorMessage = '';

  // Search
  searchName = '';

  // Add/Edit form state
  showForm = false;
  editingProduct: Product | null = null;
  formName = '';
  formDescription = '';
  formPrice: number | null = null;
  formQuantityInStock: number | null = null;
  formCategoryId: number | null = null;
  formError = '';
  submitting = false;

  ngOnInit() {
    this.fetchProducts();
    this.fetchCategories();
  }

  fetchProducts() {
    this.loading = true;
    this.errorMessage = '';
    this.productService.getAllProducts().subscribe({
      next: (data) => {
        this.products = data;
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = 'Could not load products. Please check if the backend is running.';
        this.loading = false;
        console.error(err);
      },
    });
  }

  fetchCategories() {
    this.categoryService.getAllCategories().subscribe({
      next: (data) => {
        this.categories = data;
      },
      error: (err) => console.error('Could not load categories:', err),
    });
  }

  onSearch() {
    if (!this.searchName.trim()) {
      this.fetchProducts();
      return;
    }
    this.loading = true;
    this.productService.searchProducts({ name: this.searchName.trim() }).subscribe({
      next: (data) => {
        this.products = data;
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = 'Search failed. Please try again.';
        this.loading = false;
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
    this.editingProduct = null;
    this.formName = '';
    this.formDescription = '';
    this.formPrice = null;
    this.formQuantityInStock = null;
    this.formCategoryId = this.categories.length ? this.categories[0].id : null;
    this.formError = '';
    this.showForm = true;
  }

  openEditForm(product: Product) {
    this.editingProduct = product;
    this.formName = product.name;
    this.formDescription = product.description;
    this.formPrice = product.price;
    this.formQuantityInStock = product.quantityInStock;
    this.formCategoryId = product.category?.id ?? null;
    this.formError = '';
    this.showForm = true;
  }

  closeForm() {
    this.showForm = false;
  }

  submitForm() {
    if (!this.formName || !this.formDescription || this.formPrice == null || this.formQuantityInStock == null || this.formCategoryId == null) {
      this.formError = 'Please fill in all fields.';
      return;
    }

    this.submitting = true;
    this.formError = '';

    const payload: ProductFormData = {
      name: this.formName,
      description: this.formDescription,
      price: this.formPrice,
      quantityInStock: this.formQuantityInStock,
      active: this.editingProduct ? this.editingProduct.active : true,
    };

    if (this.editingProduct) {
      this.productService.updateProduct(this.editingProduct.id, this.formCategoryId, payload).subscribe({
        next: (updated) => {
          this.submitting = false;
          this.showForm = false;
          this.products = this.products.map((p) => (p.id === updated.id ? updated : p));
        },
        error: (err) => {
          this.submitting = false;
          this.formError = 'Could not update product. Please check the fields.';
          console.error(err);
        },
      });
    } else {
      this.productService.createProduct(this.formCategoryId, payload).subscribe({
        next: (created) => {
          this.submitting = false;
          this.showForm = false;
          this.products = [...this.products, created];
        },
        error: (err) => {
          this.submitting = false;
          this.formError = 'Could not create product. Please check the fields.';
          console.error(err);
        },
      });
    }
  }

  deleteProduct(id: string) {
    this.productService.deleteProduct(id).subscribe({
      next: () => {
        this.products = this.products.filter((p) => p.id !== id);
      },
      error: (err) => {
        console.error(err);
        alert('Could not delete the product.');
      },
    });
  }
}