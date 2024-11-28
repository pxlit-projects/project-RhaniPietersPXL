import {
    Component,
    EventEmitter,
    inject,
    Input,
    OnDestroy,
    Output,
    QueryList,
    ViewChildren
} from '@angular/core';
import {Observable, Subscription} from "rxjs";
import {CommentService} from "../../../shared/services/comment.service";
import {Router} from "@angular/router";
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
    authservice: AuthService = inject(AuthService);
    router: Router = inject(Router);
    @Output() onCommentDeleted = new EventEmitter<Comment>();
    @Output() onCommentEdit = new EventEmitter<Comment>();

    sub!: Subscription;
    @Input() comment!: Comment;
    @Input() postId!: number;
    isEditing: boolean = false;

    ngOnDestroy(): void {
        if (this.sub) {
            this.sub.unsubscribe();
        }
    }

    onEdit() {
        this.isEditing = true;
    }


    onDelete() {
        this.sub = this.commentService.deleteComment(this.comment.id!).subscribe({
            next: () => {
                this.onCommentDeleted.emit(this.comment);
            }
        });
    }

    onCancelEdit() {
        this.isEditing = false;
    }

    onSubmitEdit(comment: Comment) {
        this.sub = this.commentService.updateComment(comment).subscribe({
            next: (newComment) => {
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
