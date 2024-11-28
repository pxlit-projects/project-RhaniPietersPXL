import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LoginComponent } from './login.component';
import { AuthService } from '../../shared/services/auth.service';
import { Router } from '@angular/router';
import { of } from 'rxjs';

class MockAuthService {
  setUser(username: string, role: 'redacteur' | 'lezer') {
  }
}

class MockRouter {
  navigate(commands: any[]) {
  }
}

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let mockAuthService: MockAuthService;
  let mockRouter: MockRouter;

  beforeEach(async () => {
    mockAuthService = new MockAuthService();
    mockRouter = new MockRouter();

    await TestBed.configureTestingModule({
      imports: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter },
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display an error message when username is empty', () => {
    // Arrange
    component.username = '';
    component.selectedRole = 'lezer';

    // Act
    component.login();

    // Assert
    expect(component.errorMessage).toBe('Gelieve een gebruikersnaam in te voeren.');
  });

  it('should display an error message when role is not selected', () => {
    // Arrange
    component.username = 'TestUser';
    component.selectedRole = undefined as any;

    // Act
    component.login();

    // Assert
    expect(component.errorMessage).toBe('Gelieve een gebruikersnaam in te voeren.');
  });

  it('should call authService.setUser and router.navigate when form is valid', () => {
    // Arrange
    const setUserSpy = spyOn(mockAuthService, 'setUser');
    const navigateSpy = spyOn(mockRouter, 'navigate');

    component.username = 'TestUser';
    component.selectedRole = 'redacteur';
    component.errorMessage = '';

    // Act
    component.login();

    // Assert
    expect(setUserSpy).toHaveBeenCalledWith('TestUser', 'redacteur');
    expect(navigateSpy).toHaveBeenCalledWith(['/published']);
  });

  it('should not call authService.setUser or router.navigate when form is invalid', () => {
    // Arrange
    const setUserSpy = spyOn(mockAuthService, 'setUser');
    const navigateSpy = spyOn(mockRouter, 'navigate');

    component.username = '';
    component.selectedRole = 'redacteur';

    // Act
    component.login();

    // Assert
    expect(setUserSpy).not.toHaveBeenCalled();
    expect(navigateSpy).not.toHaveBeenCalled();
    expect(component.errorMessage).toBe('Gelieve een gebruikersnaam in te voeren.');
  });
});
