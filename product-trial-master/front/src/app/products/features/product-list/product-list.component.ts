import { Component, OnInit, inject, signal } from "@angular/core";
import { Product } from "app/products/data-access/product.model";
import { ProductsService } from "app/products/data-access/products.service";
import { ProductFormComponent } from "app/products/ui/product-form/product-form.component";
import { ButtonModule } from "primeng/button";
import { CardModule } from "primeng/card";
import { DataViewModule } from 'primeng/dataview';
import { DialogModule } from 'primeng/dialog';
import { AuthService } from './../../../auth/auth.service';
import { CustomService } from '../../../shared/features/custom.service';
import { Wishlist } from '../../../shared/features/home/customInterface';

const emptyProduct: Product = {
  id: 0,
  code: "",
  name: "",
  description: "",
  image: "",
  category: "",
  price: 0,
  quantity: 0,
  internalReference: "",
  shellId: 0,
  inventoryStatus: "INSTOCK",
  rating: 0,
  createdAt: 0,
  updatedAt: 0,
};

@Component({
  selector: "app-product-list",
  templateUrl: "./product-list.component.html",
  styleUrls: ["./product-list.component.scss"],
  standalone: true,
  imports: [DataViewModule, CardModule, ButtonModule, DialogModule, ProductFormComponent],
})
export class ProductListComponent implements OnInit {
  private readonly authService = inject(AuthService);
  private readonly productsService = inject(ProductsService);
  private readonly customService = inject(CustomService);

  public readonly currentUser = signal<any>(null); // signal c comme les useref
  public readonly products = this.productsService.products;
  public wishList: Wishlist[] = [];

  public isDialogVisible = false;
  public isCreation = false;
  public readonly editedProduct = signal<Product>(emptyProduct);

  ngOnInit() {
    this.authService.currentUser.subscribe((user: any) => {
      if (user) {
        this.currentUser.set(user);
      }
    });
    this.productsService.get().subscribe();
    this.loadWishlist();
  }

  private loadWishlist(): void {
    this.customService.getUserWishList().subscribe(wishList => {
      this.wishList = wishList;
    });
  }

  public isInWishlist(productId: number): boolean {
    return this.wishList.some(item => item.product.id === productId);
  }

  public toggleWishlist(product: Product): void {
    if (this.isInWishlist(product.id)) {
      this.customService.removeFromWishlist(product.id).subscribe(() => {
        this.loadWishlist();
      });
    } else {
      this.customService.addToWishlist(product.id).subscribe(() => {
        this.loadWishlist();
      });
    }
  }

  public onCreate() {
    this.isCreation = true;
    this.isDialogVisible = true;
    this.editedProduct.set(emptyProduct);
  }

  public onUpdate(product: Product) {
    this.isCreation = false;
    this.isDialogVisible = true;
    this.editedProduct.set(product);
  }

  public onDelete(product: Product) {
    this.productsService.delete(product.id).subscribe();
  }

  public onSave(product: Product) {
    if (this.isCreation) {
      this.productsService.create(product).subscribe();
    } else {
      this.productsService.update(product).subscribe();
    }
    this.closeDialog();
  }

  public onCancel() {
    this.closeDialog();
  }

  private closeDialog() {
    this.isDialogVisible = false;
  }
}
