import { Routes } from "@angular/router";
import { HomeComponent } from "./shared/features/home/home.component";
import { LoginComponent } from "./auth/login/login.component";

export const APP_ROUTES: Routes = [
    {
    path: "login",
    component: LoginComponent
  },
  {
    path: "home",
    component: HomeComponent,
  },
  {
    path: "products",
    loadChildren: () =>
      import("./products/products.routes").then((m) => m.PRODUCTS_ROUTES)
  },
  { path: "", redirectTo: "login", pathMatch: "full" },
];
