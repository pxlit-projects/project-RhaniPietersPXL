import {Component, EventEmitter, inject, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Comment} from "../../../shared/models/comment.model";
import {NgClass} from "@angular/common";
import {CanComponentDeactivate} from "../../../confirm-leave.guard";
import {Observable} from "rxjs";

@Component({
    selector: 'app-comment-form',
    standalone: true,
    imports: [ReactiveFormsModule, NgClass],
    templateUrl: './comment-form.component.html',
    styleUrl: './comment-form.component.css'
})
export class CommentFormComponent implements OnInit, CanComponentDeactivate {
    @Input() comment!: Comment | null;
    @Output() submitComment = new EventEmitter<Comment>();
    @Output() cancelEdit = new EventEmitter<void>();

    fb: FormBuilder = inject(FormBuilder);
    commentForm: FormGroup = this.fb.group({
        title: ['', Validators.required],
        content: ['', [Validators.required]],
    });

    ngOnInit(): void {
        if (this.comment) {
            this.commentForm.patchValue({
                title: this.comment.title,
                content: this.comment.content,
            });
        }
    }

    onSubmit() {
        if (this.commentForm.valid) {
            let newComment: Comment;
            if (this.comment === null) {
                newComment = {
                    ...this.commentForm.value,
                };
            } else {
                newComment = {
                    ...this.comment,
                    ...this.commentForm.value,
                };
            }
            this.submitComment.emit(newComment);
            this.commentForm.reset();
        }
    }

    cancel() {
        this.cancelEdit.emit();
        this.commentForm.reset();
    }

    canDeactivate(): boolean | Observable<boolean> | Promise<boolean> {
        if (this.commentForm.dirty) {
            return window.confirm('Are you sure you want to leave this page?');
        }
        return true;
    }
}
