import { inject, Injectable } from "@angular/core";
import { CartItem, Wishlist } from "./home/customInterface";
import { HttpClient } from "@angular/common/http";
import { BehaviorSubject, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { MessageService } from 'primeng/api';

@Injectable({
  providedIn: 'root'
})
export class CustomService {
      private readonly http = inject(HttpClient);
      private readonly messageService = inject(MessageService);
      private readonly BASE_URL = "http://localhost:8080/api";
      private readonly TOKEN_KEY = "access_token"; // clé définie dans le intercertor.ts en gros il le get dedans
      private readonly USER_KEY = "current_user";
      private cartItemsSubject = new BehaviorSubject<CartItem[]>([]);
      public cartItems$ = this.cartItemsSubject.asObservable();
      private wishlistSubject = new BehaviorSubject<Wishlist[]>([]);
      public wishlist$ = this.wishlistSubject.asObservable();

    public getUserWishList(): Observable<Wishlist[]> {
        return this.http.get<Wishlist[]>(`${this.BASE_URL}/wishlist`).pipe(
            tap(items => this.wishlistSubject.next(items))
        );
    }

    public addToWishlist(productId: number): Observable<any> {
        return this.http.post(`${this.BASE_URL}/wishlist/${productId}`, {}).pipe(
            tap(() => this.loadWishlist())
        );
    }

    public removeFromWishlist(productId: number): Observable<any> {
        return this.http.delete(`${this.BASE_URL}/wishlist/${productId}`).pipe(
            tap(() => this.loadWishlist())
        );
    }

    private loadWishlist(): void {
        this.getUserWishList().subscribe();
    }

    public getUserCartItems(): Observable<CartItem[]> {
        return this.http.get<CartItem[]>(`${this.BASE_URL}/cart`).pipe(
            tap(items => this.cartItemsSubject.next(items))
        );
    }

    public addToCart(productId: number): Observable<any> {
        return this.http.post(`${this.BASE_URL}/cart/${productId}`, {}).pipe(
            tap(() => this.loadCartItems())
        );
    }

    public removeFromCart(productId: number): Observable<any> {
        return this.http.delete(`${this.BASE_URL}/cart/${productId}`).pipe(
            tap(() => this.loadCartItems())
        );
    }

    private loadCartItems(): void {
        this.getUserCartItems().subscribe();
    }

    public showToast(severity: string, summary: string, detail: string): void {
        this.messageService.add({ severity, summary, detail });
    }

}