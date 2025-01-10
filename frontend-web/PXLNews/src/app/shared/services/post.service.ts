import {inject, Injectable} from '@angular/core';
import {environment} from "../../../environments/environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {Post} from "../models/post.model";
import {Filter} from "../models/filter.model";
import {AuthService} from "./auth.service";

@Injectable({
    providedIn: 'root'
})

export class PostService {
    api: string = environment.apiUrlPost;
    http: HttpClient = inject(HttpClient);
    authService = inject(AuthService);

    private getHeaders(): HttpHeaders {
        return new HttpHeaders()
            .set('content-type', 'application/json')
            .set('user', this.authService.getUsername() || '')
            .set('role', this.authService.getRole() || '');
    }

    getPublishedPosts(): Observable<Post[]> {
        return this.http.get<Post[]>(this.api, {headers: this.getHeaders()});
    }

    getDrafts(author: string): Observable<Post[]> {
        return this.http.get<Post[]>(`${this.api}/drafts/${author}`, {headers: this.getHeaders()});
    }

    filterPosts(filter: Filter): Observable<Post[]> {
        return this.http.get<Post[]>(this.api, {headers: this.getHeaders()}).pipe(
            map((posts: Post[]) => posts.filter(post => this.isPostMatchingFilter(post, filter)))
        );
    }

    private isPostMatchingFilter(post: Post, filter: Filter) {
        const matchesContent = post.content.toLowerCase().includes(filter.content.toLowerCase());
        const matchesAuthor = post.author.toLowerCase().includes(filter.author.toLowerCase());
        const matchesCategory = post.category.toLowerCase().includes(filter.category.toLowerCase());
        return matchesContent && matchesAuthor && matchesCategory;
    }

    addPost(post: Post): Observable<Post> {
        return this.http.post<Post>(this.api, post, {headers: this.getHeaders()});
    }

    savePostAsDraft(newPost: Post): Observable<Post> {
        return this.http.post<Post>(this.api, newPost, {headers: this.getHeaders()});
    }

    updatePost(post: Post): Observable<Post> {
        return this.http.put<Post>(`${this.api}/${post.id}`, post, {headers: this.getHeaders()});
    }

    askApproval(id: number): Observable<Post> {
        return this.http.post<Post>(`${this.api}/${id}/approval`, null, {headers: this.getHeaders()});
    }

    publishPost(number: number): Observable<Post> {
        return this.http.post<Post>(`${this.api}/${number}/publish`, null, {headers: this.getHeaders()});
    }
}
