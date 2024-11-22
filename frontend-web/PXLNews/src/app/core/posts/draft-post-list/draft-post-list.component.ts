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
    Authservice: AuthService = inject(AuthService);
    posts!: Post[];

    ngOnInit(): void {
        this.fetchData();
    }

    private fetchData() {
        this.postService.getDrafts(this.Authservice.getUsername()!).subscribe({
            next: posts => {
                this.posts = posts;
            }
        });
    }

    onPostDetails(item: Post) {
        this.router.navigate(['/draft', item.id], {state: {post: item}});
    }
}
