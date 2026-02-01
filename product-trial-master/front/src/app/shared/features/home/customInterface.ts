import { User } from "app/auth/auth.model";

export interface Authority {
  authority: string;
}


export interface Product {
  id: number;
  code: string;
  name: string;
  description: string;
  image: string;
  category: string;
  price: number;
  quantity: number;
  internalReference: string;
  shellId: number;
  inventoryStatus: string;
  rating: number;
  createdAt: string;
  updatedAt: string;
}

export interface Wishlist {
  id: number;
  user: User;
  product: Product;
  createdAt: string;
}