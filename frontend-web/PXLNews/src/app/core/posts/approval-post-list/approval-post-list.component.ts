import {Component, inject, OnInit} from '@angular/core';
import {PostItemComponent} from "../post-item/post-item.component";
import {PostService} from "../../../shared/services/post.service";
import {Post} from "../../../shared/models/post.model";
import {AuthService} from "../../../shared/services/auth.service";

@Component({
  selector: 'app-approval-post-list',
  standalone: true,
    imports: [
        PostItemComponent
    ],
  templateUrl: './approval-post-list.component.html',
  styleUrl: './approval-post-list.component.css'
})
export class ApprovalPostListComponent implements OnInit{
  postService: PostService = inject(PostService);
  authservice: AuthService = inject(AuthService);
  posts!: Post[];

  ngOnInit(): void {
    this.fetchData();
  }

  private fetchData() {
    const author = this.authservice.getUsername();
    this.postService.getPostsToApprove(author!).subscribe({
      next: posts => {
        this.posts = posts;
      }
    });
  }
}
