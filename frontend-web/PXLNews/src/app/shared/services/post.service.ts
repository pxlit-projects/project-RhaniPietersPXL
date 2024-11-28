import {inject, Injectable} from '@angular/core';
import {environment} from "../../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {Post} from "../models/post.model";
import {Filter} from "../models/filter.model";

@Injectable({
    providedIn: 'root'
})

export class PostService {
    api: string = environment.apiUrlPost;
    http: HttpClient = inject(HttpClient);

    getPublishedPosts(): Observable<Post[]> {
        return this.http.get<Post[]>(this.api);
    }

    getDrafts(author : string) : Observable<Post[]> {
        return this.http.get<Post[]>(`${this.api}/drafts/${author}`);
    }

    filterPosts(filter: Filter): Observable<Post[]> {
        return this.http.get<Post[]>(this.api).pipe(
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
        return this.http.post<Post>(this.api, post);
    }

    savePostAsDraft(newPost: Post): Observable<Post> {
        return this.http.post<Post>(this.api, newPost);
    }

    updatePost(post: Post): Observable<Post> {
        return this.http.put<Post>(`${this.api}/${post.id}`, post);
    }

    askApproval(id: number): Observable<Post> {
        return this.http.post<Post>(`${this.api}/${id}/approval`, null);
    }

    publishPost(number: number): Observable<Post> {
        return this.http.post<Post>(`${this.api}/${number}/publish`, null);
    }
}
