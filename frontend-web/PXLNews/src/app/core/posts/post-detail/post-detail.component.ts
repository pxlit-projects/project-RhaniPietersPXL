import {Component, inject, Input, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router, RouterLink, RouterLinkActive} from "@angular/router";
import {Post} from "../../../shared/models/post.model";
import {PostService} from "../../../shared/services/post.service";
import {AuthService} from "../../../shared/services/auth.service";
import {Observable, Subscription} from "rxjs";
import {AsyncPipe} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {ConsoleLogger} from "@angular/compiler-cli";


@Component({
    selector: 'app-post-detail',
    standalone: true,
    imports: [RouterLinkActive, RouterLink, AsyncPipe, FormsModule],
    templateUrl: './post-detail.component.html',
    styleUrl: './post-detail.component.css'
})
export class PostDetailComponent implements OnDestroy {
    postService: PostService = inject(PostService);
    route: ActivatedRoute = inject(ActivatedRoute);
    router: Router = inject(Router);
    authServ: AuthService = inject(AuthService);
    post$: Observable<Post> = this.postService.getPost(this.route.snapshot.params['id']);
    sub!: Subscription;
    rejectMessage: string = '';
    showRejectInput: boolean = false;

    ngOnDestroy(): void {
        if (this.sub) {
            this.sub.unsubscribe();
        }
    }

    approvePost(id: number) {
        this.sub = this.postService.approvePost(id).subscribe({
            next: (data) => {
                this.router.navigate(['/approve']);
            }
        });
    }

    rejectPost(id: number) {
        if (!this.rejectMessage.trim()) {
            alert('Please provide a rejection message.');
            return;
        }
        this.sub = this.postService.rejectPost(id, this.rejectMessage).subscribe({
            next: (data) => {
                this.router.navigate(['/approve']);
            }
        });
    }

    toggleRejectInput() {
        this.showRejectInput = !this.showRejectInput;
    }

    publishPost(number: number) {
        this.sub = this.postService.publishPost(number).subscribe({
            next: (data) => {
                this.router.navigate(['/posts']);
            }
        });

    }
}