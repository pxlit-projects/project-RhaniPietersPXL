import {Component, Input, input} from '@angular/core';
import {Post} from "../../../shared/models/post.model";

@Component({
  selector: 'app-detail',
  standalone: true,
  imports: [],
  templateUrl: './detail.component.html',
  styleUrl: './detail.component.css'
})
export class DetailComponent {
  @Input() post!: Post;
}
