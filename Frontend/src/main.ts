import { bootstrapApplication } from '@angular/platform-browser';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { App } from './app/app';
import routeConfig from './app/app.routes';
import { authInterceptor } from './app/interceptors/auth-interceptor';


bootstrapApplication(App, {
  providers: [
    provideRouter(routeConfig), 
    provideHttpClient(withInterceptors([authInterceptor])),
  ],
}).catch((err) => console.error(err));
