import {inject, Injectable} from '@angular/core';
import {environment} from "../../../environments/environment.development";
import {HttpClient} from "@angular/common/http";
import {AuthService} from "./auth.service";
import {Observable} from "rxjs";
import {Post} from "../models/post.model";

@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  api: string = environment.apiUrlReview;
  http: HttpClient = inject(HttpClient);
  authServ: AuthService = inject(AuthService);

  getPublishedPosts(): Observable<Post[]> {
    return this.http.get<Post[]>(this.api);
  }
}
