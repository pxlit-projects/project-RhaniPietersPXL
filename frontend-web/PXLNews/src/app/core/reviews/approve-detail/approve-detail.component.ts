import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {AsyncPipe} from "@angular/common";
import {DetailComponent} from "../../posts/detail/detail.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {Router, RouterLinkActive} from "@angular/router";
import {Subscription} from "rxjs";
import {Post} from "../../../shared/models/post.model";
import {AuthService} from "../../../shared/services/auth.service";
import {ReviewService} from "../../../shared/services/review.service";

@Component({
    selector: 'app-approve-detail',
    standalone: true,
    imports: [AsyncPipe, DetailComponent, ReactiveFormsModule, RouterLinkActive, FormsModule],
    templateUrl: './approve-detail.component.html',
    styleUrl: './approve-detail.component.css'
})
export class ApproveDetailComponent implements OnInit, OnDestroy {
    reviewService: ReviewService = inject(ReviewService);
    router: Router = inject(Router);
    authServ: AuthService = inject(AuthService);
    sub!: Subscription;

    post!: Post;
    rejectMessage: string = '';
    showRejectInput: boolean = false;

    ngOnInit(): void {
        this.post = history.state['post'];
    }

    approvePost(id: number) {
        this.sub = this.reviewService.approvePost(id).subscribe({
            next: () => {
                this.router.navigate(['/approve']);
            }
        });
    }

    rejectPost(id: number) {
        //TODO: Add validation
        if (!this.rejectMessage.trim()) {
            alert('Please provide a rejection message.');
            return;
        }
        this.sub = this.reviewService.rejectPost(id, this.rejectMessage).subscribe({
            next: () => {
                this.router.navigate(['/approve']);
            }
        });
    }

    toggleRejectInput() {
        this.showRejectInput = !this.showRejectInput;
    }

    ngOnDestroy(): void {
        if (this.sub) {
            this.sub.unsubscribe();
        }
    }
}
