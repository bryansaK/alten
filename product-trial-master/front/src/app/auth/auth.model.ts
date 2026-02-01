export interface User {
  id?: number | string;
  email?: string;
  firstname?: string;
  username?: string;
}

export interface RegisterResponse {
  message?: string;
  user?: User;
}

export interface LoginResponse {
  token: string;
  username?: string;
  email?: string;
  fisrtname?: string;
  role?: string;
}