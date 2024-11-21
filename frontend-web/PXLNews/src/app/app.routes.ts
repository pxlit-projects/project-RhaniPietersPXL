import { Routes } from '@angular/router';
import {AddPostComponent} from "./core/posts/add-post/add-post.component";
import {EditPostComponent} from "./core/posts/edit-post/edit-post.component";
import {PostDetailComponent} from "./core/posts/post-detail/post-detail.component";
import {LoginComponent} from "./core/login/login.component";
import {PublishedPostListComponent} from "./core/posts/published-post-list/published-post-list.component";
import {DraftPostListComponent} from "./core/posts/draft-post-list/draft-post-list.component";
import {ApprovalPostListComponent} from "./core/posts/approval-post-list/approval-post-list.component";

export const routes: Routes = [
    {path: 'login', component: LoginComponent},
    {path: 'posts', component: PublishedPostListComponent},
    {path: 'drafts', component: DraftPostListComponent},
    {path: 'approve', component: ApprovalPostListComponent},
    {path: 'add', component: AddPostComponent},
    {path: 'edit', component: EditPostComponent},
    {path: 'post/:id', component: PostDetailComponent},
    {path: '', redirectTo: 'login', pathMatch: 'full'},
];

