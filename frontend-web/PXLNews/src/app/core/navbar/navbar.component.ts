import {Component, inject} from '@angular/core';
import {Router, RouterLink, RouterLinkActive} from "@angular/router";
import {NgClass, NgIf} from "@angular/common";
import {AuthService} from "../../shared/services/auth.service";

@Component({
    selector: 'app-navbar',
    standalone: true,
    imports: [RouterLink, RouterLinkActive, NgClass, NgIf],
    templateUrl: './navbar.component.html',
    styleUrl: './navbar.component.css'
})
export class NavbarComponent {
    isMenuOpen = false;
    authService = inject(AuthService);
    router = inject(Router);

    toggleMenu() {
        this.isMenuOpen = !this.isMenuOpen;
    }

    closeMenu() {
        this.isMenuOpen = false;
    }

    logout() {
        this.authService.logout();
        this.router.navigate(['/login']);
    }
}
