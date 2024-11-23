import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {DetailComponent} from "../../posts/detail/detail.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {Router} from "@angular/router";
import {Observable, Subscription} from "rxjs";
import {Post} from "../../../shared/models/post.model";
import {CommentService} from "../../../shared/services/comment.service";
import {Comment} from "../../../shared/models/comment.model";
import {CommentComponent} from "../comment/comment.component";
import {PostItemComponent} from "../../posts/post-item/post-item.component";
import {NgClass} from "@angular/common";
import {AuthService} from "../../../shared/services/auth.service";
import {CommentFormComponent} from "../comment-form/comment-form.component";

@Component({
    selector: 'app-comment-detail',
    standalone: true,
    imports: [DetailComponent, FormsModule, CommentComponent, PostItemComponent, ReactiveFormsModule, NgClass, CommentFormComponent],
    templateUrl: './comment-detail.component.html',
    styleUrl: './comment-detail.component.css'
})
export class CommentDetailComponent implements OnInit, OnDestroy  {
    commentService: CommentService = inject(CommentService);
    authService: AuthService = inject(AuthService);
    router: Router = inject(Router);
    sub!: Subscription;
    isUserLoggedIn: boolean = this.authService.getUsername() != null;

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

    onSubmitAdd(newComment: Comment) {
        this.showCommentInput = false;
        newComment.author = this.authService.getUsername()!;
        newComment.postId = this.post.id!;
        this.sub = this.commentService.commentOnPost(newComment).subscribe({
            next: (comment) => {
                this.comments.push(comment);
            }
        });
    }

    onDelete(item: Comment) {
        this.comments = this.comments.filter(comment => comment.id !== item.id);
    }

    onCancelAdd() {
        this.showCommentInput = false;

    }

    onEdit(updatedComment: Comment) {
        const commentIndex = this.comments.findIndex(comment => comment.id === updatedComment.id);
        this.comments[commentIndex] = updatedComment;
    }
}
