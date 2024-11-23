import {inject, Injectable} from '@angular/core';
import {environment} from "../../../environments/environment.development";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Comment} from "../models/comment.model";

@Injectable({
    providedIn: 'root'
})
export class CommentService {
    api: string = environment.apiUrlComment;
    http: HttpClient = inject(HttpClient);

    commentOnPost(comment: Comment): Observable<Comment> {
        return this.http.post<Comment>(`${this.api}/${comment.postId}`, comment);
    }

    getComments(id: number): Observable<Comment[]> {
        return this.http.get<Comment[]>(`${this.api}/${id}`);
    }

    deleteComment(number: number): Observable<Comment> {
        return this.http.delete<Comment>(`${this.api}/${number}`);
    }

    updateComment(updatedComment: Comment): Observable<Comment> {
        return this.http.put<Comment>(`${this.api}/${updatedComment.id}`, updatedComment);
    }
}
