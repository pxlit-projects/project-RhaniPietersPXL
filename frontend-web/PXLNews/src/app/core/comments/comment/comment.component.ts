import {Component, inject, Input, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {CommentService} from "../../../shared/services/comment.service";
import {Router} from "@angular/router";
import {AuthService} from "../../../shared/services/auth.service";
import {Comment} from "../../../shared/models/comment.model";

@Component({
  selector: 'app-comment',
  standalone: true,
  imports: [],
  templateUrl: './comment.component.html',
  styleUrl: './comment.component.css'
})
export class CommentComponent implements OnDestroy{
  commentService: CommentService = inject(CommentService);
  authservice: AuthService = inject(AuthService);
  router: Router = inject(Router);

  sub!: Subscription;
  @Input() comment!: Comment;
  @Input() postId!: number;

  ngOnDestroy(): void {
    if (this.sub) {
      this.sub.unsubscribe();
    }
  }

  onEdit() {
    //navigeer naar edit pagina
  }

  onDelete() {
    this.sub = this.commentService.deleteComment(this.comment.id!).subscribe({
      next: () => {
        this.router.navigate(['/published', this.postId]);
      }
    });
  }
}
