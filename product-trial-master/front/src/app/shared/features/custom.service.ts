import { inject, Injectable } from "@angular/core";
import { Wishlist } from "./home/customInterface";
import { HttpClient } from "@angular/common/http";
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CustomService {
      private readonly http = inject(HttpClient);
      private readonly BASE_URL = "http://localhost:8080/api";
      private readonly TOKEN_KEY = "access_token"; // clé définie dans le intercertor.ts en gros il le get dedans
      private readonly USER_KEY = "current_user";

    public getUserWishList(): Observable<Wishlist[]> {
        return this.http.get<Wishlist[]>(`${this.BASE_URL}/wishlist`);
    }

    public addToWishlist(productId: number): Observable<any> {
        return this.http.post(`${this.BASE_URL}/wishlist/${productId}`, {});
    }

    public removeFromWishlist(productId: number): Observable<any> {
        return this.http.delete(`${this.BASE_URL}/wishlist/${productId}`);
    }

}