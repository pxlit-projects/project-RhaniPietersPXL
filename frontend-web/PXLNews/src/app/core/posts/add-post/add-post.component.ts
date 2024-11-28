import {Component, inject} from '@angular/core';
import {PostService} from "../../../shared/services/post.service";
import {Router} from "@angular/router";
import {FormsModule} from "@angular/forms";
import {Post} from "../../../shared/models/post.model";
import {NgClass, NgIf} from "@angular/common";
import {AuthService} from "../../../shared/services/auth.service";
import {CanComponentDeactivate} from "../../../confirm-leave.guard";
import {Observable} from "rxjs";

@Component({
    selector: 'app-add-post',
    standalone: true,
    imports: [FormsModule, NgClass, NgIf],
    templateUrl: './add-post.component.html',
    styleUrl: './add-post.component.css'
})
export class AddPostComponent implements CanComponentDeactivate {
    postService: PostService = inject(PostService);
    authService: AuthService = inject(AuthService);
    router: Router = inject(Router);
    action: string = ''

    setAction(action: string): void {
        this.action = action;
    }

    postForm: Post = {
        creationDate: "",
        state: "",
        title: '',
        content: '',
        category: '',
        author: this.authService.getUsername()!
    };

    onSubmit(data: Object): void {
        if (this.postForm.title && this.postForm.content && this.postForm.category) {
            const newPost: Post = {
                ...this.postForm,
                creationDate: new Date().toISOString(),
                state: this.action === 'approval' ? 'PENDING_APPROVAL' : 'DRAFT',
            };

            if (this.action === 'approval') {
                this.postService.addPost(newPost).subscribe(() => {
                    this.resetForm();
                    this.router.navigate(['/drafts']);
                });
            } else if (this.action === 'draft') {
                this.postService.savePostAsDraft(newPost).subscribe(() => {
                    this.resetForm();
                    this.router.navigate(['/drafts']);
                });
            }
        }
    }

    resetForm(): void {
        this.postForm = {
            creationDate: "",
            state: "",
            title: '',
            content: '',
            category: '',
            author: this.authService.getUsername()!
        };
    }

    canDeactivate(): boolean | Observable<boolean> | Promise<boolean> {
        if (this.postForm.title || this.postForm.content || this.postForm.category) {
            return window.confirm('Are you sure you want to leave this page?');
        }
        return true;
    }
}
