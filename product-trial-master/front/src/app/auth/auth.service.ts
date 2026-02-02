import { inject, Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable, BehaviorSubject } from "rxjs";
import { tap } from "rxjs/operators";
import { LoginResponse, RegisterResponse, User } from "./auth.model";
import { Login } from './login/login.model';
import { MessageService } from 'primeng/api';



@Injectable({
  providedIn: "root",
})
export class AuthService {
  private readonly messageService = inject(MessageService);
  private readonly http = inject(HttpClient);
  private readonly BASE_URL = "http://localhost:8080/api";
  private readonly TOKEN_KEY = "access_token"; // clé définie dans le intercertor.ts en gros il le get dedans
  private readonly USER_KEY = "current_user";


  private isAuthenticatedSubject = new BehaviorSubject<boolean>(
    this.hasToken()
  );
  public isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  private currentUserSubject = new BehaviorSubject<User | null>(
    this.getStoredUser()
  );
  public currentUser = this.currentUserSubject.asObservable();


  private hasToken(): boolean {
    return !!localStorage.getItem(this.TOKEN_KEY);
  }

  private getStoredUser(): User | null {
    const stored = localStorage.getItem(this.USER_KEY);
    return stored ? (JSON.parse(stored) as User) : null;
  }

  private setStoredUser(user: User | null): void {
    if (user) {
      localStorage.setItem(this.USER_KEY, JSON.stringify(user));
    } else {
      localStorage.removeItem(this.USER_KEY);
    }
  }


  public getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  public setCurrentUser(user: User | null): void {
    this.currentUserSubject.next(user);
    this.setStoredUser(user);
  }


  public login(email: string, password: string): Observable<LoginResponse> {
    return this.http
      .post<LoginResponse>(this.BASE_URL + "/token", { email, password })
      .pipe(
        tap((response: LoginResponse) => {
          localStorage.setItem(this.TOKEN_KEY, response.token);
          this.isAuthenticatedSubject.next(true);
          if (response) {
            console.log('Login response:', response);
            this.setCurrentUser({
              email: response.email,
              firstname: response.firstname,
              username: response.username
            });
            this.messageService.add({severity:'success', summary: 'Succès', detail: 'Connexion réussie'});
          } else {
             this.messageService.add({severity:'error', summary: 'Erreur', detail: 'Échec de la connexion'});
          }
        })
      );
  }

    public register(email: string, password: string, firstname?: string, username?: string): Observable<RegisterResponse> {
    return this.http
      .post<RegisterResponse>(this.BASE_URL + "/account", { email, password, firstname, username })
      .pipe(
        tap((response) => {
          if (response.user) {
            this.setCurrentUser(response.user);
            this.messageService.add({severity:'success', summary: 'Succès', detail: 'Inscription réussie'});
          }
        })
      );
  }

  public logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    this.isAuthenticatedSubject.next(false);
    this.setCurrentUser(null);
  }


  public isAuthenticated(): boolean {
    return this.hasToken();
  }
}
