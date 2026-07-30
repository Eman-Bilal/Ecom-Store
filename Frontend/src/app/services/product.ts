import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environments';

// Full shape returned by create / update / reactivate endpoints
export interface Product {
  id: string;
  name: string;
  description: string;
  price: number;
  quantityInStock: number;
  image: string | null;
  contentType: string | null;
  categoryName: string | null;
  active: boolean;
}

// Shape returned by getAll / getByCategory / getById / search
export interface ProductResponse {
  id: string;
  name: string;
  description: string;
  price: number;
  quantityInStock: number;
  image: string | null;
  contentType: string | null;
  categoryName: string | null;
  active: boolean;
}

export interface ProductFormData {
  name: string;
  description: string;
  price: number;
  quantityInStock: number;
  active: boolean;
}

export interface ProductRequestDto {
  name: string;
  description: string;
  price: number;
  quantityInStock: number;
}

export function getProductImageSrc(product: { image: string | null; contentType: string | null }): string | null {
  if (!product.image) return null;
  if (product.image.startsWith('data:')) return product.image;
  const mime = product.contentType || 'image/jpeg';
  return `data:${mime};base64,${product.image}`;
}

@Injectable({ providedIn: 'root' })
export class ProductService {
  private baseUrl = `${environment.apiUrl}/products`;

  constructor(private http: HttpClient) {}

  getAllProducts(): Observable<ProductResponse[]> {
    return this.http.get<ProductResponse[]>(this.baseUrl);
  }

  getByCategory(categoryId: number): Observable<ProductResponse[]> {
    return this.http.get<ProductResponse[]>(`${this.baseUrl}/category/${categoryId}`);
  }

  getById(id: string): Observable<ProductResponse> {
    return this.http.get<ProductResponse>(`${this.baseUrl}/${id}`);
  }

  searchProducts(filters: {
    name?: string;
    minPrice?: number;
    maxPrice?: number;
    categoryId?: number;
    sortBy?: string;
  }): Observable<ProductResponse[]> {
    let params = new HttpParams();
    if (filters.name) params = params.set('name', filters.name);
    if (filters.minPrice != null) params = params.set('minPrice', filters.minPrice);
    if (filters.maxPrice != null) params = params.set('maxPrice', filters.maxPrice);
    if (filters.categoryId != null) params = params.set('categoryId', filters.categoryId);
    if (filters.sortBy) params = params.set('sortBy', filters.sortBy);

    return this.http.get<ProductResponse[]>(`${this.baseUrl}/search`, { params });
  }

  createProductWithImage(
    categoryId: number,
    product: ProductRequestDto,
    file: File
  ): Observable<Product> {
    const formData = new FormData();
    const productBlob = new Blob([JSON.stringify(product)], { type: 'application/json' });
    formData.append('product', productBlob);
    formData.append('file', file);

    return this.http.post<Product>(`${this.baseUrl}/category/${categoryId}/image`, formData);
  }

  updateProduct(id: string, categoryId: number, product: ProductFormData): Observable<Product> {
    return this.http.put<Product>(`${this.baseUrl}/${id}/${categoryId}`, product);
  }

  updateProductImage(id: string, file: File): Observable<Product> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.put<Product>(`${this.baseUrl}/${id}/image`, formData);
  }

  deleteProduct(id: string): Observable<string> {
    return this.http.delete(`${this.baseUrl}/${id}`, { responseType: 'text' });
  }

  reactivateProduct(id: string): Observable<Product> {
    return this.http.patch<Product>(`${this.baseUrl}/${id}`, {});
  }
}