import { Routes } from '@angular/router';
import { CustomerLayout } from './layout/customer-layout/customer-layout';
import { AdminLayout } from './layout/admin-layout/admin-layout';
import { Home } from './customer/home/home';
import { Shop } from './customer/shop/shop';
import { ProductDetail } from './customer/shop/product-detail/product-detail';
import { Cart } from './customer/cart/cart';
import { Checkout } from './customer/checkout/checkout';
import { About } from './customer/about/about';
import { Contact } from './customer/contact/contact';
import { AdminDashboard } from './admin/admin-dashboard/admin-dashboard';
import { AdminProducts } from './admin/admin-products/admin-products';
import { adminModeGuard } from './guards/admin-mode-guard';
import { Register } from './admin/register/register';

const routeConfig: Routes = [
  {
    path: '',
    component: CustomerLayout,
    children: [
      { path: '', component: Home },
      { path: 'shop', component: Shop },
      { path: 'product/:id', component: ProductDetail },
      { path: 'cart', component: Cart },
      { path: 'checkout', component: Checkout },
      { path: 'about', component: About },
      { path: 'contact', component: Contact },
    ],
  },
  {
    path: 'admin',
    component: AdminLayout,
    canActivate: [adminModeGuard],
    children: [
      { path: 'dashboard', component: AdminDashboard },
      { path: 'products', component: AdminProducts },
      { path: 'register', component: Register },
    ],
  },
];

export default routeConfig;