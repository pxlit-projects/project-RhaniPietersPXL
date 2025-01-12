import {inject, Injectable} from '@angular/core';
import {environment} from "../../../environments/environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {Review} from "../models/review.model";
import {AuthService} from "./auth.service";

@Injectable({providedIn: 'root'})

export class ReviewService {
    api: string = environment.apiUrlReview;
    http: HttpClient = inject(HttpClient);
    authService : AuthService = inject(AuthService);

    private getHeaders(): HttpHeaders {
        return new HttpHeaders()
            .set('content-type', 'application/json')
            .set('role', this.authService.getRole() || '');
    }

    getPostsToApprove(author: string): Observable<Review[]> {
        return this.http.get<Review[]>(`${this.api}/approval/${author}`, {headers: this.getHeaders()});
    }

    approvePost(id: number): Observable<Review> {
        return this.http.post<Review>(`${this.api}/${id}/approve`, null, {headers: this.getHeaders()});
    }

    rejectPost(id: number, message: string): Observable<Review> {
        return this.http.post<Review>(`${this.api}/${id}/reject`, message, {headers: this.getHeaders()});
    }
}
