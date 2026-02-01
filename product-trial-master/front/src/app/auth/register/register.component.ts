import { Component, inject } from "@angular/core";
import { Register } from "./register.model";
import { ButtonModule } from "primeng/button";
import { FormsModule } from "@angular/forms";
import { AuthService } from "../auth.service";
import { Router } from "@angular/router";


const emptyUser: Register = {
    password: "",
    email: "",
    firstName: "",
    userName: "",
};
export interface RegisterResponse {
    message: string;
}

@Component({
    providers: [AuthService],
  selector: "app-register",
  templateUrl: "./register.component.html",
    styleUrls: ["./register.component.scss"],
    standalone: true,
    imports: [ButtonModule, FormsModule],
})
export class RegisterComponent {
    router: Router = inject(Router);
    public registerData: Register = emptyUser;
    constructor(private authService: AuthService) {}

    public onRegister() {
        if (this.registerData.email && this.registerData.password) {
            this.authService.register(this.registerData.email, this.registerData.password, this.registerData.firstName, this.registerData.userName).subscribe({
                next: (response: RegisterResponse) => {
                    console.log("Register successful", response);
                    this.router.navigate(['/login']);
                }
            });
        }
    }

    public toLogin() {
        this.router.navigate(['/login']);
    }
}
