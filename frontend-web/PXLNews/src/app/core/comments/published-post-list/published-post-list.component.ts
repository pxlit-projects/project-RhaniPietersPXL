import {Component, inject, OnInit} from '@angular/core';
import {PostItemComponent} from "../../posts/post-item/post-item.component";
import {AddPostComponent} from "../../posts/add-post/add-post.component";
import {FilterComponent} from "../../posts/filter/filter.component";
import {PostService} from "../../../shared/services/post.service";
import {Post} from "../../../shared/models/post.model";
import {Filter} from "../../../shared/models/filter.model";
import {Router} from "@angular/router";

@Component({
    selector: 'app-published-post-list',
    standalone: true,
    imports: [PostItemComponent, AddPostComponent, FilterComponent],
    templateUrl: './published-post-list.component.html',
    styleUrl: './published-post-list.component.css'
})
export class PublishedPostListComponent implements OnInit {
    postService: PostService = inject(PostService);
    router: Router = inject(Router);
    filteredData!: Post[];

    ngOnInit(): void {
        this.fetchData();
    }

    private fetchData() {
        this.postService.getPublishedPosts().subscribe({
            next: posts => {
                this.filteredData = posts;
            }
        });
    }

    handleFilter(filter: Filter) {
        this.postService.filterPosts(filter).subscribe({
            next: posts => this.filteredData = posts
        });
    }

    onPostDetails(item: Post) {
        this.router.navigate(['/published', item.id], {state: {post: item}});
    }
}
