import {
    Component,
    inject,
  } from "@angular/core";
import { MenuItem } from "primeng/api";
  import { PanelMenuModule } from 'primeng/panelmenu';
  import { AuthService } from '../../../auth/auth.service';
  import { Router } from '@angular/router';
  
  @Component({
    selector: "app-panel-menu",
    standalone: true,
    imports: [PanelMenuModule],
    template: `
        <p-panelMenu [model]="items" styleClass="w-full" />
    `
  })
  export class PanelMenuComponent {
    private readonly authService = inject(AuthService);
    private readonly router = inject(Router);

    public readonly items: MenuItem[] = [
        {
            label: 'Accueil',
            icon: 'pi pi-home',
            routerLink: ['/home']
        },
        {
            label: 'Produits',
            icon: 'pi pi-barcode',
            routerLink: ['/products/list']
        },
        {
            label: 'Déconnexion',
            icon: 'pi pi-sign-out',
            command: () => this.logout()
        }
    ]

    logout(): void {
      this.authService.logout();
      this.router.navigate(['/login']);
    }
  }
  