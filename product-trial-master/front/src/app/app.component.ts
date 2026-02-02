import {
  Component,
  inject,
} from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { SplitterModule } from 'primeng/splitter';
import { ToolbarModule } from 'primeng/toolbar';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { PanelMenuComponent } from "./shared/ui/panel-menu/panel-menu.component";
import { CustomService } from "./shared/features/custom.service";
import { CartItem } from "./shared/features/home/customInterface";
import { AuthService } from "./auth/auth.service";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.scss"],
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    SplitterModule,
    ToolbarModule,
    ToastModule,
    PanelMenuComponent,
  ],
  providers: [MessageService],
})
export class AppComponent {
  title = "ALTEN SHOP";
  private readonly authService = inject(AuthService);
 currentUser = this.authService.currentUser;
  private readonly customService = inject(CustomService);
  private readonly messageService = inject(MessageService);
  cartItems: CartItem[] = [];
  public isCartOpen = false;

  ngOnInit(): void {
    this.customService.getUserCartItems().subscribe();
    this.customService.cartItems$.subscribe(cartItems => {
      this.cartItems = cartItems;
    });
  }

  openCartPopup(): void {
    if (this.authService.isAuthenticated()) {
    this.isCartOpen = true;
    }
  }

  closeCartPopup(): void {
    this.isCartOpen = false;
  }

  showToast(severity: string, summary: string, detail: string): void {
    this.messageService.add({ severity, summary, detail });
  }
}
