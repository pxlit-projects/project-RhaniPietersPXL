import {Component, Input} from '@angular/core';
import {Post} from "../../../shared/models/post.model";
import {RouterLink, RouterLinkActive} from "@angular/router";

@Component({
  selector: 'app-post-item',
  standalone: true,
    imports: [
        RouterLink,
        RouterLinkActive
    ],
  templateUrl: './post-item.component.html',
  styleUrl: './post-item.component.css'
})
export class PostItemComponent {
  @Input() post!: Post;
}
