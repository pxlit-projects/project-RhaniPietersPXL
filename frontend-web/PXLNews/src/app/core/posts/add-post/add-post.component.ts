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

    fb: FormBuilder = inject(FormBuilder);
    postForm: FormGroup = this.fb.group({
        title: ['', Validators.required],
        content: ['', [Validators.required]],
        category: ['', Validators.required],
    });

    onSubmit() {
        const newPost: Post = {...this.postForm.value};
        this.postService.addPost(newPost).subscribe(post => {
            this.postForm.reset();
            this.router.navigate(['/drafts']);
        });
    }

    publishPost() {
        const newPost: Post = {
            ...this.postForm.value,
            state: 'PENDING_APPROVAL',
            creationDate: new Date().toISOString(),
            author: this.authService.getUsername()
        };
        this.postService.savePostAsDraft(newPost).subscribe(post => {
            this.postForm.reset();
            this.router.navigate(['/drafts']);
        });
    }

    savePostAsDraft() {
        const newPost: Post = {
            ...this.postForm.value,
            state: 'DRAFT',
            creationDate: new Date().toISOString(),
            author: this.authService.getUsername()
        };
        this.postService.submitPostForApproval(newPost).subscribe(post => {
            this.postForm.reset();
            this.router.navigate(['/posts']);
        });
    }
}
