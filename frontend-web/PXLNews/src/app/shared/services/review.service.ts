import {inject, Injectable} from '@angular/core';
import {environment} from "../../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Review} from "../models/review.model";

@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  api: string = environment.apiUrlReview;
  http: HttpClient = inject(HttpClient);

  getPostsToApprove(author: string): Observable<Review[]> {
    return this.http.get<Review[]>(`${this.api}/approval/${author}`);
  }
  approvePost(id: number): Observable<Review> {
    return this.http.post<Review>(`${this.api}/${id}/approve`, null);
  }

  rejectPost(id: number, message: string): Observable<Review> {
    return this.http.post<Review>(`${this.api}/${id}/reject`, message);
  }
}
