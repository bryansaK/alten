import { Component, inject, OnInit } from "@angular/core";
import { RouterLink } from "@angular/router";
import { ButtonModule } from "primeng/button";
import { CardModule } from "primeng/card";
import { AuthService } from "./../../../auth/auth.service";
import { CommonModule } from "@angular/common";
import { CustomService } from "../custom.service";
import { Wishlist } from "./customInterface";

@Component({
  selector: "app-home",
  templateUrl: "./home.component.html",
  styleUrls: ["./home.component.scss"],
  standalone: true,
  imports: [CardModule, RouterLink, ButtonModule, CommonModule],
})
export class HomeComponent implements OnInit {
  private readonly authService = inject(AuthService);
  private readonly customService = inject(CustomService);
  
  public currentUser = this.authService.currentUser;
  public wishList: Wishlist[] = [];
  public readonly appTitle = "ALTEN SHOP";

  ngOnInit(): void {
    this.customService.getUserWishList().subscribe(wishList => {
      this.wishList = wishList;
    });
  }
}
