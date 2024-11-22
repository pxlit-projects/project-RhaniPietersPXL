import {Component, inject, OnInit} from '@angular/core';
import {PostItemComponent} from "../post-item/post-item.component";
import {Post} from "../../../shared/models/post.model";
import {AuthService} from "../../../shared/services/auth.service";
import {Router} from "@angular/router";
import {ReviewService} from "../../../shared/services/review.service";
import {Review} from "../../../shared/models/review.model";

@Component({
  selector: 'app-approval-post-list',
  standalone: true,
    imports: [PostItemComponent],
  templateUrl: './approval-post-list.component.html',
  styleUrl: './approval-post-list.component.css'
})
export class ApprovalPostListComponent implements OnInit{
  reviewService: ReviewService = inject(ReviewService);
  authservice: AuthService = inject(AuthService);
  router: Router = inject(Router);
  reviews!: Review[];

  ngOnInit(): void {
    this.fetchData();
  }

  private fetchData() {
    const author = this.authservice.getUsername();
    this.reviewService.getPostsToApprove(author!).subscribe({
      next: reviews => {
        this.reviews = reviews;
      }
    });
  }

  onPostDetails(item: Post) {
    this.router.navigate(['/approve', item.id], {state: {post: item}});
  }
}
