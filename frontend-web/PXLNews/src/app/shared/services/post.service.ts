import {inject, Injectable} from '@angular/core';
import {environment} from "../../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {Post} from "../models/post.model";
import {Filter} from "../models/filter.model";
import {AuthService} from "./auth.service";

@Injectable({
    providedIn: 'root'
})
//  ng serve --proxy-config src/proxy.conf.json
export class PostService {
    api: string = environment.apiUrl;
    http: HttpClient = inject(HttpClient);
    authServ: AuthService = inject(AuthService);

    getPublishedPosts(): Observable<Post[]> {
        return this.http.get<Post[]>(this.api);
    }
    getDrafts() : Observable<Post[]> {
        let author = this.authServ.getUsername();
        return this.http.get<Post[]>(`${this.api}/drafts/${author}`);
    }

    getPostsToApprove(author: string): Observable<Post[]> {
        return this.http.get<Post[]>(`${this.api}/approval/${author}`);
    }

    filterPosts(filter: Filter): Observable<Post[]> {
        return this.http.get<Post[]>(this.api).pipe(
            map((customers: Post[]) => customers.filter(customer => this.isPostMatchingFilter(customer, filter)))
        );
    }

    private isPostMatchingFilter(post: Post, filter: Filter) {
        const matchesContent = post.content.toLowerCase().includes(filter.content.toLowerCase());
        const matchesAuthor = post.author.toLowerCase().includes(filter.author.toLowerCase());
        const matchesTitle = post.title.toLowerCase().includes(filter.author.toLowerCase());

        return matchesContent && matchesAuthor && matchesTitle;
    }

    addPost(post: Post): Observable<Post> {
        return this.http.post<Post>(this.api, post);
    }

    savePostAsDraft(newPost: Post): Observable<Post> {
        return this.http.post<Post>(this.api, newPost);
    }

    getPost(id: number) : Observable<Post> {
        return this.http.get<Post>(`${this.api}/${id}`);
    }

    updatePost(post: Post): Observable<Post> {
        return this.http.put<Post>(`${this.api}/${post.id}`, post);
    }

    askApproval(id: number): Observable<Post> {
        return this.http.post<Post>(`${this.api}/${id}/approval`, null);
    }

    approvePost(id: number): Observable<Post> {
        return this.http.post<Post>(`${this.api}/${id}/approve`, null);
    }

    rejectPost(id: number, message: string): Observable<Post> {
        const rejectRequest = { message };
        return this.http.post<Post>(`${this.api}/${id}/reject`, rejectRequest);
    }

    publishPost(number: number): Observable<Post> {
        return this.http.post<Post>(`${this.api}/${number}/publish`, null);
    }
}
