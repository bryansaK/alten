import { inject, Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable, BehaviorSubject } from "rxjs";
import { tap } from "rxjs/operators";
import { RegisterResponse } from "./register/register.component";

export interface LoginResponse {
  token: string;
}

@Injectable({
  providedIn: "root",
})
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly BASE_URL = "http://localhost:3000/api";
  private readonly TOKEN_KEY = "access_token"; // clé définie dans le intercertor.ts en gros il le get dedans


  private isAuthenticatedSubject = new BehaviorSubject<boolean>(
    this.hasToken()
  );
  public isAuthenticated$ = this.isAuthenticatedSubject.asObservable();


  private hasToken(): boolean {
    return !!localStorage.getItem(this.TOKEN_KEY);
  }


  public getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }


  public login(email: string, password: string): Observable<LoginResponse> {
    return this.http
      .post<LoginResponse>(this.BASE_URL + "/token", { email, password })
      .pipe(
        tap((response) => {
          localStorage.setItem(this.TOKEN_KEY, response.token);
          this.isAuthenticatedSubject.next(true);
        })
      );
  }

    public register(email: string, password: string, firstName?: string, userName?: string): Observable<RegisterResponse> {
    return this.http
      .post<RegisterResponse>(this.BASE_URL + "/account", { email, password, firstName, userName });
  }

  public logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    this.isAuthenticatedSubject.next(false);
  }


  public isAuthenticated(): boolean {
    return this.hasToken();
  }
}
