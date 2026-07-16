import { Routes } from '@angular/router';
import { CustomerLayout } from './layout/customer-layout/customer-layout';
import { AdminLayout } from './layout/admin-layout/admin-layout';
import { Home } from './home/home';
import { Shop } from './shop/shop';
import { ProductDetail } from './product-detail/product-detail';
import { Cart } from './cart/cart';
import { Checkout } from './checkout/checkout';
import { About } from './about/about';
import { Contact } from './contact/contact';
import { AdminDashboard } from './admin/admin-dashboard/admin-dashboard';
import { AdminProducts } from './admin/admin-products/admin-products';
import { adminModeGuard } from './guards/admin-mode-guard';

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
    ],
  },
];

export default routeConfig;