import {Routes} from '@angular/router';
import {AddPostComponent} from "./core/posts/add-post/add-post.component";
import {EditPostComponent} from "./core/posts/edit-post/edit-post.component";
import {LoginComponent} from "./core/login/login.component";
import {PublishedPostListComponent} from "./core/comments/published-post-list/published-post-list.component";
import {DraftPostListComponent} from "./core/posts/draft-post-list/draft-post-list.component";
import {ApprovalPostListComponent} from "./core/reviews/approval-post-list/approval-post-list.component";
import {DraftDetailComponent} from "./core/posts/draft-detail/draft-detail.component";
import {CommentDetailComponent} from "./core/comments/comment-detail/comment-detail.component";
import {ApproveDetailComponent} from "./core/reviews/approve-detail/approve-detail.component";
import {confirmLeaveGuard} from "./confirm-leave.guard";

export const routes: Routes = [
    {path: 'login', component: LoginComponent,},
    {path: 'published', component: PublishedPostListComponent},
    {
        path: 'published/:id', component: CommentDetailComponent, canDeactivate: [confirmLeaveGuard],
    },
    {path: 'drafts', component: DraftPostListComponent},
    {path: 'draft/:id', component: DraftDetailComponent},
    {path: 'edit/:id', component: EditPostComponent, canDeactivate: [confirmLeaveGuard]},

    {path: 'approve', component: ApprovalPostListComponent},
    {path: 'approve/:id', component: ApproveDetailComponent},

    {path: 'add', component: AddPostComponent, canDeactivate: [confirmLeaveGuard]},

    {path: '', redirectTo: 'login', pathMatch: 'full'},
    {path: '**', redirectTo: 'login'}

];

