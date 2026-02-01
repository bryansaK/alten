export interface User {
  id?: number | string;
  email?: string;
  firstName?: string;
  userName?: string;
}

export interface RegisterResponse {
  message?: string;
  user?: User;
}

export interface LoginResponse {
  token: string;
  user?: User;
}
