import {Component, inject} from '@angular/core';
import {PostService} from "../../../shared/services/post.service";
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Post} from "../../../shared/models/post.model";
import {NgClass, NgIf} from "@angular/common";
import {AuthService} from "../../../shared/services/auth.service";

@Component({
    selector: 'app-add-post',
    standalone: true,
    imports: [
        ReactiveFormsModule,
        NgClass,
        NgIf
    ],
    templateUrl: './add-post.component.html',
    styleUrl: './add-post.component.css'
})
export class AddPostComponent {
    postService: PostService = inject(PostService);
    authService: AuthService = inject(AuthService);
    router: Router = inject(Router);
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

    onSubmit(): void {
        if (this.postForm.valid) {
            const newPost: Post = {
                ...this.postForm.value,
                author: this.authService.getUsername(),
                creationDate: new Date().toISOString(),
                state: this.action === 'publish' ? 'PENDING_APPROVAL' : 'DRAFT',
            };

            if (this.action === 'publish') {
                this.postService.addPost(newPost).subscribe(post => {
                    this.postForm.reset();
                    this.router.navigate(['/drafts']);
                });
            } else if (this.action === 'draft') {
                this.postService.savePostAsDraft(newPost).subscribe(post => {
                    this.postForm.reset();
                    this.router.navigate(['/drafts']);
                });
            }
        }
    }
}
