import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environments';
import { Category } from './category';

export interface Product {
  id: string;
  name: string;
  description: string;
  price: number;
  quantityInStock: number;
  status: 'IN_STOCK' | 'OUT_OF_STOCK';
  active: boolean;
  category: Category;
}

export interface ProductFormData {
  name: string;
  description: string;
  price: number;
  quantityInStock: number;
  active: boolean;
}

@Injectable({ providedIn: 'root' })
export class ProductService {
  private baseUrl = `${environment.apiUrl}/products`;

  constructor(private http: HttpClient) {}

  getAllProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.baseUrl);
  }

  getById(id: string): Observable<Product> {
    return this.http.get<Product>(`${this.baseUrl}/${id}`);
  }

  createProduct(categoryId: number, product: ProductFormData): Observable<Product> {
    return this.http.post<Product>(`${this.baseUrl}/${categoryId}`, product);
  }

  updateProduct(id: string, categoryId: number, product: ProductFormData): Observable<Product> {
    return this.http.put<Product>(`${this.baseUrl}/${id}/${categoryId}`, product);
  }

   deleteProduct(id: string): Observable<string> {
    return this.http.delete(`${this.baseUrl}/${id}`, { responseType: 'text' });
  }
 
  // Reactivates a previously deactivated product (sets active = true)
  reactivateProduct(id: string): Observable<Product> {
    return this.http.patch<Product>(`${this.baseUrl}/${id}`, {});
  }

  searchProducts(filters: {
    name?: string;
    minPrice?: number;
    maxPrice?: number;
    categoryId?: number;
    sortBy?: string;
   }): Observable<Product[]> {
    let params = new HttpParams();
    if (filters.name) params = params.set('name', filters.name);
    if (filters.minPrice != null) params = params.set('minPrice', filters.minPrice);
    if (filters.maxPrice != null) params = params.set('maxPrice', filters.maxPrice);
    if (filters.categoryId != null) params = params.set('categoryId', filters.categoryId);
    if (filters.sortBy) params = params.set('sortBy', filters.sortBy);

    return this.http.get<Product[]>(`${this.baseUrl}/search`, { params });
  }
}