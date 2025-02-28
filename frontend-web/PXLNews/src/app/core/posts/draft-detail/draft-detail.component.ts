import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {ReactiveFormsModule} from "@angular/forms";
import {Router, RouterLink, RouterLinkActive} from "@angular/router";
import {PostService} from "../../../shared/services/post.service";
import {AuthService} from "../../../shared/services/auth.service";
import {Subscription} from "rxjs";
import {Post} from "../../../shared/models/post.model";
import {DetailComponent} from "../detail/detail.component";

@Component({
    selector: 'app-draft-detail',
    standalone: true,
    imports: [ReactiveFormsModule, RouterLinkActive, RouterLink, DetailComponent],
    templateUrl: './draft-detail.component.html',
    styleUrl: './draft-detail.component.css'
})
export class DraftDetailComponent implements OnInit, OnDestroy {
    postService: PostService = inject(PostService);
    router: Router = inject(Router);
    authService: AuthService = inject(AuthService);
    post!: Post;
    sub!: Subscription;

    ngOnInit(): void {
        this.post = history.state['post'];
    }

    ngOnDestroy(): void {
        if (this.sub) {
            this.sub.unsubscribe();
        }
    }

    publishPost(number: number): void {
        this.sub = this.postService.publishPost(number).subscribe({
            next: () => {
                this.router.navigate(['/published']);
            }
        });
    }

    editPost(number: number): void {
        this.router.navigate(['/edit', number], {state: {post: this.post}});

    }
}
