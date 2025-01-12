import {Component, inject, OnDestroy, OnInit, ViewChild, ViewChildren} from '@angular/core';
import {DetailComponent} from "../../posts/detail/detail.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {Observable, Subscription} from "rxjs";
import {Post} from "../../../shared/models/post.model";
import {CommentService} from "../../../shared/services/comment.service";
import {Comment} from "../../../shared/models/comment.model";
import {CommentComponent} from "../comment/comment.component";
import {PostItemComponent} from "../../posts/post-item/post-item.component";
import {NgClass} from "@angular/common";
import {AuthService} from "../../../shared/services/auth.service";
import {CommentFormComponent} from "../comment-form/comment-form.component";
import {CanComponentDeactivate} from "../../../confirm-leave.guard";

@Component({
    selector: 'app-comment-detail',
    standalone: true,
    imports: [DetailComponent, FormsModule, CommentComponent, PostItemComponent, ReactiveFormsModule, NgClass, CommentFormComponent],
    templateUrl: './comment-detail.component.html',
    styleUrl: './comment-detail.component.css'
})
export class CommentDetailComponent implements OnInit, OnDestroy, CanComponentDeactivate {
    @ViewChild(CommentFormComponent) formComponent!: CommentFormComponent;

    commentService: CommentService = inject(CommentService);
    authService: AuthService = inject(AuthService);
    sub!: Subscription;
    isUserLoggedIn: boolean = this.authService.getUsername() != null;

    post!: Post;
    comments: Comment[] = [];
    showCommentInput: boolean = false;


    ngOnInit(): void {
        this.post = history.state['post'];
        this.sub = this.commentService.getComments(this.post.id!).subscribe({
            next: (comments : Comment[]) => {
                this.comments = comments;
            }
        });
    }

    ngOnDestroy(): void {
        if (this.sub) {
            this.sub.unsubscribe();
        }
    }

    toggleAddComment(): void {
        this.showCommentInput = !this.showCommentInput;
    }

    onSubmitAdd(newComment: Comment) {
        this.showCommentInput = false;
        newComment.author = this.authService.getUsername()!;
        newComment.postId = this.post.id!;
        this.sub = this.commentService.commentOnPost(newComment).subscribe({
            next: (comment : Comment): void => {
                this.comments.push(comment);
            }
        });
    }

    onDelete(item: Comment): void {
        this.comments = this.comments.filter(comment => comment.id !== item.id);
    }

    onCancelAdd(): void {
        this.showCommentInput = false;

    }

    onEdit(updatedComment: Comment) : void {
        const commentIndex : number = this.comments.findIndex(comment => comment.id === updatedComment.id);
        this.comments[commentIndex] = updatedComment;
    }

    canDeactivate(): boolean | Observable<boolean> | Promise<boolean> {
        if (this.formComponent && this.formComponent.commentForm.dirty) {
            return window.confirm('Are you sure you want to leave this page?');
        }
        return true;
    }
}
