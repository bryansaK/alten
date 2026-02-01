import { Component } from "@angular/core";
import { Login } from "./login.model";
import { ButtonModule } from "primeng/button";
import { FormsModule } from "@angular/forms";


const emptyUser: Login = {
    password: "",
    email: "",
};

@Component({
  selector: "app-login",
  templateUrl: "./login.component.html",
    styleUrls: ["./login.component.scss"],
    standalone: true,
    imports: [ButtonModule, FormsModule],
})
export class LoginComponent {
    public loginData: Login = emptyUser;

    public onLogin() {
        console.log("Login data:", this.loginData);
    }
}