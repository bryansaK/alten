import { Routes } from "@angular/router";
import { HomeComponent } from "./shared/features/home/home.component";
import { LoginComponent } from "./auth/login/login.component";
import { hasRoleGuard } from "./auth/auth.guard";
import { RegisterComponent } from "./auth/register/register.component";

export const APP_ROUTES: Routes = [
    {
    path: "login",
    component: LoginComponent
  },
   {
    path: "register",
    component: RegisterComponent
  },
  {
    path: "home",
    component: HomeComponent,
    canActivate: [hasRoleGuard]
  },
  {
    canActivate: [hasRoleGuard],
    path: "products",
    loadChildren: () =>
      import("./products/products.routes").then((m) => m.PRODUCTS_ROUTES)
  },
  { path: "", redirectTo: "login", pathMatch: "full" },
];
