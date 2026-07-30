import { Component, OnInit, inject, signal } from '@angular/core';
import { DecimalPipe, SlicePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  ProductService,
  Product,
  ProductResponse,
  ProductFormData,
  ProductRequestDto,
} from '../../services/product';
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
  products = signal<ProductResponse[]>([]);
  categories = signal<Category[]>([]);
  loading = signal(true);
  errorMessage = signal('');
  showForm = signal(false);
  submitting = signal(false);
  formError = signal('');
  editingProduct = signal<ProductResponse | null>(null);

  // Plain fields for ngModel-bound inputs (typing works fine either way)
  searchName = '';
  formName = '';
  formDescription = '';
  formPrice: number | null = null;
  formQuantityInStock: number | null = null;
  formCategoryId: number | null = null;

  // Image upload state
  selectedImageFile: File | null = null;
  imagePreviewUrl: string | null = null;
  imageError = signal('');
  private readonly allowedImageTypes = ['image/jpeg', 'image/png', 'image/jpg'];

  ngOnInit() {
    this.fetchProducts();
    this.fetchCategories();
  }

  // GET /api/products
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

  // GET /api/products/search
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

  onImageSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) return;

    const file = input.files[0];
    this.imageError.set('');

    if (!this.allowedImageTypes.includes(file.type)) {
      this.imageError.set('Only JPG, JPEG, and PNG image formats are allowed.');
      input.value = ''; // clear the invalid selection
      this.selectedImageFile = null;
      return;
    }

    this.selectedImageFile = file;
    this.imagePreviewUrl = URL.createObjectURL(file);
  }

  // Converts the base64 "image" + "contentType" fields from ProductResponseDto
  // into a displayable data URL for <img [src]>
  getImageSrc(product: ProductResponse): string | null {
    if (!product.image) return null;
    if (product.image.startsWith('data:')) return product.image;
    const mime = product.contentType || 'image/jpeg';
    return `data:${mime};base64,${product.image}`;
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
    this.imageError.set('');
    this.selectedImageFile = null;
    this.imagePreviewUrl = null;
    this.showForm.set(true);
  }

  openEditForm(product: ProductResponse) {
    this.editingProduct.set(product);
    this.formName = product.name;
    this.formDescription = product.description;
    this.formPrice = product.price;
    this.formQuantityInStock = product.quantityInStock;

    // Backend only sends categoryName (no categoryId on ProductResponseDto),
    // so we match it against the loaded categories list to pre-select the dropdown.
    const matchedCategory = this.categories().find(
      (c) => c.categoryName === product.categoryName
    );
    this.formCategoryId = matchedCategory ? matchedCategory.id : null;

    this.formError.set('');
    this.imageError.set('');
    this.selectedImageFile = null;
    this.imagePreviewUrl = this.getImageSrc(product); // show existing image, if any
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

    if (currentlyEditing) {
      // Editing an existing product
      const payload: ProductFormData = {
        name: this.formName,
        description: this.formDescription,
        price: this.formPrice,
        quantityInStock: this.formQuantityInStock,
        active: currentlyEditing.active ?? true,
      };

      this.productService.updateProduct(currentlyEditing.id, this.formCategoryId, payload).subscribe({
        next: (updated) => this.afterEditSave(updated),
        error: (err) => {
          this.submitting.set(false);
          this.formError.set('Could not update product. Please check the fields.');
          console.error(err);
        },
      });
      return;
    }

    // Creating a new product — image is required since we only use the
    // combined create-with-image endpoint now.
    if (!this.selectedImageFile) {
      this.submitting.set(false);
      this.imageError.set('Please select a product image.');
      return;
    }

    const requestDto: ProductRequestDto = {
      name: this.formName,
      description: this.formDescription,
      price: this.formPrice,
      quantityInStock: this.formQuantityInStock,
    };

    this.productService
      .createProductWithImage(this.formCategoryId, requestDto, this.selectedImageFile)
      .subscribe({
        next: (created) => {
          this.submitting.set(false);
          this.showForm.set(false);
          this.fetchProducts(); // re-fetch so the new product's DTO fields (image/categoryName) are correct
        },
        error: (err) => {
          this.submitting.set(false);
          this.formError.set('Could not create product. Please check the fields.');
          console.error(err);
        },
      });
  }

  // Runs after an existing product's regular fields have been updated.
  // If the user picked a new image, replace it now via the image endpoint;
  // otherwise we're already done.
  private afterEditSave(product: Product) {
    if (!this.selectedImageFile) {
      this.submitting.set(false);
      this.showForm.set(false);
      this.fetchProducts();
      return;
    }

    this.productService.updateProductImage(product.id, this.selectedImageFile).subscribe({
      next: () => {
        this.submitting.set(false);
        this.showForm.set(false);
        this.fetchProducts();
      },
      error: (err) => {
        this.submitting.set(false);
        this.formError.set('Product saved, but the image could not be updated.');
        console.error(err);
        this.showForm.set(false);
        this.fetchProducts();
      },
    });
  }

  toggleStatus(product: ProductResponse) {
    if (product.active) {
      this.productService.deleteProduct(product.id).subscribe({
        next: () => {
          // DELETE only returns a plain text message, not the updated product,
          // so we manually flip the active flag in local state.
          this.products.update((list) =>
            list.map((p) => (p.id === product.id ? { ...p, active: false } : p))
          );
        },
        error: (err) => {
          console.error(err);
          alert('Could not deactivate the product.');
        },
      });
    } else {
      this.productService.reactivateProduct(product.id).subscribe({
        next: () => {
          // Backend returns full Product entity here, not ProductResponseDto,
          // so we just re-fetch to get the consistent DTO shape.
          this.fetchProducts();
        },
        error: (err) => {
          console.error(err);
          alert('Could not activate the product.');
        },
      });
    }
  }
}