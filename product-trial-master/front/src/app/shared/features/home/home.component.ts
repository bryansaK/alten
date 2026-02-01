import { Component, inject, OnInit } from "@angular/core";
import { RouterLink } from "@angular/router";
import { ButtonModule } from "primeng/button";
import { CardModule } from "primeng/card";
import { AuthService } from "./../../../auth/auth.service";
import { User } from "./../../../auth/auth.model";

@Component({
  selector: "app-home",
  templateUrl: "./home.component.html",
  styleUrls: ["./home.component.scss"],
  standalone: true,
  imports: [CardModule, RouterLink, ButtonModule],
})
export class HomeComponent implements OnInit {
  private readonly authService = inject(AuthService);
  public currentUser: User | null = null;
  public readonly appTitle = "ALTEN SHOP";

  ngOnInit(): void {
    this.authService.currentUser$.subscribe((user) => {
      this.currentUser = user;
    });
  }
}
