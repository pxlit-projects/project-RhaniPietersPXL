import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {PostService} from "../../../shared/services/post.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Observable, Subscription} from "rxjs";
import {Post} from "../../../shared/models/post.model";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {AsyncPipe, NgClass} from "@angular/common";
import {CanComponentDeactivate} from "../../../confirm-leave.guard";
import {data} from "autoprefixer";

@Component({
    selector: 'app-edit-post',
    standalone: true,
    imports: [ReactiveFormsModule, NgClass, AsyncPipe],
    templateUrl: './edit-post.component.html',
    styleUrl: './edit-post.component.css'
})
export class EditPostComponent implements OnInit, OnDestroy, CanComponentDeactivate {
    postService: PostService = inject(PostService);
    router: Router = inject(Router);
    route: ActivatedRoute = inject(ActivatedRoute);
    id: number = this.route.snapshot.params['id'];
    post!: Post;
    sub!: Subscription;
    action: string = ''

    setAction(action: string): void {
        this.action = action;
    }

    fb: FormBuilder = inject(FormBuilder);
    postForm: FormGroup = this.fb.group({
        title: ['', Validators.required],
        content: ['', [Validators.required]],
        category: ['', Validators.required],
    });

    ngOnInit(): void {
        this.post = history.state['post'];
        this.postForm.patchValue({
            title: this.post.title,
            content: this.post.content,
            category: this.post.category
        });
    }

    ngOnDestroy(): void {
        if (this.sub) {
            this.sub.unsubscribe();
        }
    }

    onSubmit(): void {
        if (this.postForm.valid) {
            const updatedPost = {...this.post, ...this.postForm.value};
            if (this.postForm.dirty) {
                this.editPost(updatedPost);
            } else if (this.action === 'approval') {
                this.askApproval(this.id);
            } else {
                this.router.navigate(['/draft', this.id], {state: {post: this.post}});
            }
        }
    }

    editPost(post: Post) {
        this.sub = this.postService.updatePost(post).subscribe({
            next: (data) => {
                this.postForm.reset();
                this.post = data;
                if (this.action === 'approval') {
                    this.askApproval(this.id);
                } else {
                    this.router.navigate(['/draft', this.id], {state: {post: this.post}});
                }
            }
        });
    }

    askApproval(id: number): void {
        this.sub = this.postService.askApproval(id).subscribe({
            next: (data) => {
                this.postForm.reset();
                this.post = data;
                this.router.navigate(['/draft', this.id], {state: {post: this.post}});
            }
        });
    }

    cancel(): void {
        this.router.navigate(['/draft', this.id], {state: {post: this.post}});
    }

    canDeactivate(): boolean | Observable<boolean> | Promise<boolean> {
        if (this.postForm.dirty) {
            return window.confirm('Are you sure you want to leave this page?');
        }
        return true;
    }
}


