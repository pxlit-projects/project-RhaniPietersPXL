import { Routes } from '@angular/router';
import {AddPostComponent} from "./core/posts/add-post/add-post.component";
import {EditPostComponent} from "./core/posts/edit-post/edit-post.component";
import {LoginComponent} from "./core/login/login.component";
import {PublishedPostListComponent} from "./core/posts/published-post-list/published-post-list.component";
import {DraftPostListComponent} from "./core/posts/draft-post-list/draft-post-list.component";
import {ApprovalPostListComponent} from "./core/posts/approval-post-list/approval-post-list.component";
import {DraftDetailComponent} from "./core/posts/draft-detail/draft-detail.component";
import {CommentDetailComponent} from "./core/posts/comment-detail/comment-detail.component";
import {ApproveDetailComponent} from "./core/posts/approve-detail/approve-detail.component";

export const routes: Routes = [
    {path: 'login', component: LoginComponent,},
    {path: 'published', component: PublishedPostListComponent},
    {path: 'published/:id', component: CommentDetailComponent},
    {path: 'drafts', component: DraftPostListComponent},
    {path: 'draft/:id', component: DraftDetailComponent},
    {path: 'edit/:id', component: EditPostComponent},

    {path: 'approve', component: ApprovalPostListComponent},
    {path: 'approve/:id', component: ApproveDetailComponent},

    {path: 'add', component: AddPostComponent},
    {path: '', redirectTo: 'login', pathMatch: 'full'},
];

