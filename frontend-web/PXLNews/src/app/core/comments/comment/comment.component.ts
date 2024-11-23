import {Component, EventEmitter, inject, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {Subscription} from "rxjs";
import {CommentService} from "../../../shared/services/comment.service";
import {Router} from "@angular/router";
import {AuthService} from "../../../shared/services/auth.service";
import {Comment} from "../../../shared/models/comment.model";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {NgClass} from "@angular/common";

@Component({
    selector: 'app-comment',
    standalone: true,
    imports: [
        ReactiveFormsModule,
        NgClass
    ],
    templateUrl: './comment.component.html',
    styleUrl: './comment.component.css'
})
export class CommentComponent implements OnInit, OnDestroy {
    commentService: CommentService = inject(CommentService);
    authservice: AuthService = inject(AuthService);
    router: Router = inject(Router);
    @Output() commentAdded = new EventEmitter<Comment>();


    sub!: Subscription;
    @Input() comment!: Comment;
    @Input() postId!: number;
    isEditing: boolean = false;
    editedContent!: string;
    editedTitle!: string;

    fb: FormBuilder = inject(FormBuilder);
    commentForm: FormGroup = this.fb.group({
        title: ['', Validators.required],
        content: ['', [Validators.required]],
    });

    ngOnInit(): void {
        if (this.comment) {
            this.commentForm.patchValue({
                title: this.comment.title,
                content: this.comment.content
            });
        }
    }

    ngOnDestroy(): void {
        if (this.sub) {
            this.sub.unsubscribe();
        }
    }

    onEdit() {
        this.isEditing = true;
        this.commentForm.enable();
        this.editedContent = this.comment.content;
        this.editedTitle = this.comment.title;
    }

    onSubmit() {
        if (this.commentForm.valid) {
            if (this.comment) {
                const updatedComment = {...this.comment, ...this.commentForm.value,};

                this.sub = this.commentService
                    .updateComment(updatedComment)
                    .subscribe({
                        next: (updatedComment) => {
                            this.comment = updatedComment;
                            this.isEditing = false;
                            this.commentForm.disable();
                        },
                    });
            } else {
                const newComment = {...this.commentForm.value, postId: this.postId};
                this.sub = this.commentService.commentOnPost(newComment).subscribe({
                    next: (comment) => {
                        this.commentForm.reset();
                        this.commentAdded.emit(comment);
                    }
                });
            }
        }
    }

    onCancel() {
        this.isEditing = false;
        this.editedContent = this.comment.content;
        this.editedTitle = this.comment.title;
    }

    onDelete() {
        this.sub = this.commentService.deleteComment(this.comment.id!).subscribe({
            next: () => {
                this.router.navigate(['/published', this.postId]);
            }
        });
    }
}
