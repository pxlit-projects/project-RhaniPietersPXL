import {inject, Injectable} from '@angular/core';
import {environment} from "../../../environments/environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {Comment} from "../models/comment.model";
import {AuthService} from "./auth.service";

@Injectable({
    providedIn: 'root'
})
export class CommentService {
    api: string = environment.apiUrlComment;
    http: HttpClient = inject(HttpClient);
    authService : AuthService = inject(AuthService);

    private getHeaders(): HttpHeaders {
        return new HttpHeaders()
            .set('content-type', 'application/json')
            .set('user', this.authService.getUsername() || '')
    }

    commentOnPost(comment: Comment): Observable<Comment> {
        return this.http.post<Comment>(`${this.api}/${comment.postId}`, comment, {headers: this.getHeaders()});
    }

    getComments(id: number): Observable<Comment[]> {
        return this.http.get<Comment[]>(`${this.api}/${id}`, {headers: this.getHeaders()});
    }

    deleteComment(number: number): Observable<Comment> {
        return this.http.delete<Comment>(`${this.api}/${number}`, {headers: this.getHeaders()});
    }

    updateComment(updatedComment: Comment): Observable<Comment> {
        return this.http.put<Comment>(`${this.api}/${updatedComment.id}`, updatedComment, {headers: this.getHeaders()});
    }
}
