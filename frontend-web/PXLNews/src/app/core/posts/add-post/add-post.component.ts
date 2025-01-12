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

    newPost: Post = {
        creationDate: "",
        state: "",
        title: '',
        content: '',
        category: '',
        author: this.authService.getUsername()!
    };

    onSubmit(): void {
        const post: Post = {
            ...this.newPost,
            creationDate: new Date().toISOString(),
            state: this.action === 'approval' ? 'PENDING_APPROVAL' : 'DRAFT',
        };

        if (this.action === 'approval') {
            this.postService.addPost(post).subscribe((): void => {
                this.resetForm();
                this.router.navigate(['/drafts']);
            });
        } else if (this.action === 'draft') {
            this.postService.savePostAsDraft(post).subscribe((): void => {
                this.resetForm();
                this.router.navigate(['/drafts']);
            });
        }

    }

    resetForm(): void {
        this.newPost = {
            creationDate: "",
            state: "",
            title: '',
            content: '',
            category: '',
            author: this.authService.getUsername()!
        };
    }

    canDeactivate(): boolean | Observable<boolean> | Promise<boolean> {
        if (this.newPost.title || this.newPost.content || this.newPost.category) {
            return window.confirm('Are you sure you want to leave this page?');
        }
        return true;
    }
}
