import {Component, inject, OnInit} from '@angular/core';
import {PostService} from "../../../shared/services/post.service";
import {Post} from "../../../shared/models/post.model";
import {PostItemComponent} from "../post-item/post-item.component";
import {AuthService} from "../../../shared/services/auth.service";
import {Router} from "@angular/router";

@Component({
    selector: 'app-draft-post-list',
    standalone: true,
    imports: [PostItemComponent],
    templateUrl: './draft-post-list.component.html',
    styleUrl: './draft-post-list.component.css'
})
export class DraftPostListComponent implements OnInit {
    postService: PostService = inject(PostService);
    router: Router = inject(Router);
    authService: AuthService = inject(AuthService);
    posts!: Post[];

    ngOnInit(): void {
        this.fetchData();
    }

    private fetchData(): void {
        this.postService.getDrafts(this.authService.getUsername()!).subscribe({
            next: posts => {
                this.posts = posts;
            }
        });
    }

    onPostDetails(item: Post): void {
        this.router.navigate(['/draft', item.id], {state: {post: item}});
    }
}
