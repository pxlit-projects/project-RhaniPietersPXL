import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {DetailComponent} from "../../posts/detail/detail.component";
import {FormsModule} from "@angular/forms";
import {Router} from "@angular/router";
import {Subscription} from "rxjs";
import {Post} from "../../../shared/models/post.model";
import {CommentService} from "../../../shared/services/comment.service";
import {Comment} from "../../../shared/models/comment.model";
import {CommentComponent} from "../comment/comment.component";
import {PostItemComponent} from "../../posts/post-item/post-item.component";

@Component({
    selector: 'app-comment-detail',
    standalone: true,
    imports: [DetailComponent, FormsModule, CommentComponent, PostItemComponent],
    templateUrl: './comment-detail.component.html',
    styleUrl: './comment-detail.component.css'
})
export class CommentDetailComponent implements OnInit, OnDestroy {
    commentService: CommentService = inject(CommentService);
    router: Router = inject(Router);
    sub!: Subscription;

    post!: Post;
    comments: Comment[] = [];
    showCommentInput: boolean = false;

    ngOnInit(): void {
        this.post = history.state['post'];
        this.sub = this.commentService.getComments(this.post.id!).subscribe({
            next: (comments) => {
                this.comments = comments;
            }
        });
    }

    ngOnDestroy(): void {
        if (this.sub) {
            this.sub.unsubscribe();
        }
    }

    toggleAddComment() {
        this.showCommentInput = !this.showCommentInput;
    }

    onCommentAdded($event: Comment) {
        this.showCommentInput = false;
        this.comments.push($event);
    }
}
