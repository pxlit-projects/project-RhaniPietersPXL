import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private username: string | null = null;
  private role: 'redacteur' | 'lezer' | null = null;

  constructor() { }

  setUser(username: string, role: 'redacteur' | 'lezer'): void {
    this.username = username;
    this.role = role;
  }

  getUsername(): string | null {
    return this.username;
  }

  getRole(): 'redacteur' | 'lezer' | null {
    return this.role;
  }

  isLoggedIn(): boolean {
    return this.username !== null && this.role !== null;
  }

  logout(): void {
    this.username = null;
    this.role = null;
  }
}
