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

    constructor() {
    }

    getPublishedPosts(): Observable<Post[]> {
        return this.http.get<Post[]>(this.api);
    }

    addPost(post: Post): Observable<Post> {
        return this.http.post<Post>(this.api, post);
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

    savePostAsDraft(newPost: Post): Observable<Post> {
        return this.http.post<Post>(this.api, newPost);

    }

    submitPostForApproval(newPost: Post): Observable<Post> {
        return this.http.post<Post>(this.api, newPost);

    }

    getDrafts() : Observable<Post[]> {
        let author = this.authServ.getUsername();
        return this.http.get<Post[]>(`${this.api}/drafts/${author}`);
    }
}
