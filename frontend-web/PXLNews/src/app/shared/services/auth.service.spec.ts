import { TestBed } from '@angular/core/testing';
import { AuthService } from './auth.service';

describe('AuthService', () => {
  let service: AuthService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuthService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should set the user correctly', () => {
    service.setUser('johnDoe', 'redacteur');
    expect(service.getUsername()).toBe('johnDoe');
    expect(service.getRole()).toBe('redacteur');
  });

  it('should return null for username and role if not set', () => {
    expect(service.getUsername()).toBeNull();
    expect(service.getRole()).toBeNull();
  });

  it('should return true if user is logged in', () => {
    service.setUser('johnDoe', 'lezer');
    expect(service.isLoggedIn()).toBeTrue();
  });

  it('should return false if user is not logged in', () => {
    expect(service.isLoggedIn()).toBeFalse();
  });

  it('should log out the user correctly', () => {
    service.setUser('johnDoe', 'redacteur');
    service.logout();
    expect(service.getUsername()).toBeNull();
    expect(service.getRole()).toBeNull();
    expect(service.isLoggedIn()).toBeFalse();
  });
});
