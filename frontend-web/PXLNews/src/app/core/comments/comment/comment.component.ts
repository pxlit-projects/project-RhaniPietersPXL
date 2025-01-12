import {Component, EventEmitter, inject, Input, OnDestroy, Output, QueryList, ViewChildren} from '@angular/core';
import {Observable, Subscription} from "rxjs";
import {CommentService} from "../../../shared/services/comment.service";
import {AuthService} from "../../../shared/services/auth.service";
import {Comment} from "../../../shared/models/comment.model";
import {ReactiveFormsModule} from "@angular/forms";
import {NgClass} from "@angular/common";
import {CommentFormComponent} from "../comment-form/comment-form.component";
import {CanComponentDeactivate} from "../../../confirm-leave.guard";

@Component({
    selector: 'app-comment',
    standalone: true,
    imports: [ReactiveFormsModule, NgClass, CommentFormComponent],
    templateUrl: './comment.component.html',
    styleUrl: './comment.component.css'
})
export class CommentComponent implements OnDestroy, CanComponentDeactivate {
    @ViewChildren(CommentFormComponent) formComponents!: QueryList<CommentFormComponent>;

    commentService: CommentService = inject(CommentService);
    authService: AuthService = inject(AuthService);
    @Output() onCommentDeleted : EventEmitter<Comment> = new EventEmitter<Comment>();
    @Output() onCommentEdit : EventEmitter<Comment> = new EventEmitter<Comment>();

    sub!: Subscription;
    @Input() comment!: Comment;
    @Input() postId!: number;
    isEditing: boolean = false;

    ngOnDestroy(): void {
        if (this.sub) {
            this.sub.unsubscribe();
        }
    }

    onEdit() : void {
        this.isEditing = true;
    }

    onDelete() : void {
        this.sub = this.commentService.deleteComment(this.comment.id!).subscribe({
            next: () : void => {
                this.onCommentDeleted.emit(this.comment);
            }
        });
    }

    onCancelEdit() : void {
        this.isEditing = false;
    }

    onSubmitEdit(comment: Comment) : void {
        this.sub = this.commentService.updateComment(comment).subscribe({
            next: (newComment : Comment) => {
                this.comment = newComment;
                this.onCommentEdit.emit(newComment);
                this.isEditing = false;
            }
        });
    }

    canDeactivate(): boolean | Observable<boolean> | Promise<boolean> {
        for (let form of this.formComponents.toArray()) {
            if (form.commentForm.dirty) {
                return window.confirm('Are you sure you want to leave this page?');
            }
        }
        return true;
    }

}
