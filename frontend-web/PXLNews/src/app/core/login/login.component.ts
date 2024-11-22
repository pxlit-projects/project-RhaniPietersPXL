import {Component} from '@angular/core';
import {AuthService} from "../../shared/services/auth.service";
import {Router} from "@angular/router";
import {FormsModule} from "@angular/forms";
import {NgIf} from "@angular/common";

@Component({
    selector: 'app-login',
    standalone: true,
    imports: [
        FormsModule,
        NgIf
    ],
    templateUrl: './login.component.html',
    styleUrl: './login.component.css'
})
export class LoginComponent {
    username: string = '';
    selectedRole: 'redacteur' | 'lezer' = 'lezer';
    errorMessage: string = '';

    constructor(private authService: AuthService, private router: Router) {
    }

    login(): void {
        if (this.username && this.selectedRole) {
            this.errorMessage = '';
            this.authService.setUser(this.username, this.selectedRole);
            this.router.navigate(['/published']);
        } else {
            this.errorMessage = 'Gelieve een gebruikersnaam in te voeren.';
        }
    }
}
