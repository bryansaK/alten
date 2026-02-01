import { Component, inject } from "@angular/core";
import { Login } from "./login.model";
import { ButtonModule } from "primeng/button";
import { FormsModule } from "@angular/forms";
import { AuthService } from "../auth.service";
import { Router } from "@angular/router";


const emptyUser: Login = {
    password: "",
    email: "",
};

@Component({
    providers: [AuthService],
  selector: "app-login",
  templateUrl: "./login.component.html",
    styleUrls: ["./login.component.scss"],
    standalone: true,
    imports: [ButtonModule, FormsModule],
})
export class LoginComponent {
    router: Router = inject(Router);
    public loginData: Login = emptyUser;
    constructor(private authService: AuthService) {}

    public onLogin() {
        if (this.loginData.email && this.loginData.password) {
            this.authService.login(this.loginData.email, this.loginData.password).subscribe({
                next: (response) => {
                    this.router.navigate(['/home']);
                }
            });
        }
    }

    public toRegister() {
        this.router.navigate(['/register']);
    }
}