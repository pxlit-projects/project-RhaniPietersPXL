import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PublishedPostListComponent } from './published-post-list.component';
import { PostService } from '../../../shared/services/post.service';
import { Post } from '../../../shared/models/post.model';

describe('PublishedPostListComponent', () => {
    let component: PublishedPostListComponent;
    let fixture: ComponentFixture<PublishedPostListComponent>;
    let postServiceMock: jasmine.SpyObj<PostService>;

    beforeEach(() => {
        postServiceMock = jasmine.createSpyObj('PostService', ['getPublishedPosts', 'filterPosts']);

        TestBed.configureTestingModule({
            imports: [PublishedPostListComponent],
            providers: [
                { provide: PostService, useValue: postServiceMock }
            ]
        });

        fixture = TestBed.createComponent(PublishedPostListComponent);
        component = fixture.componentInstance;
    });

    it('should create the component', () => {
        expect(component).toBeTruthy();
    });



    it('should call fetchData on initialization', () => {
        spyOn(component, 'fetchData');
        fixture.detectChanges();
        expect(component.fetchData).toHaveBeenCalled();
    });
    it('should navigate to post details on post click', () => {
        const post: Post = new Post('Title 1', 'Content 1', 'Author 1', '2023-11-28', 'Category 1', 'published');
        spyOn(component.router, 'navigate');

        // Act
        component.onPostDetails(post);

        // Assert
        expect(component.router.navigate).toHaveBeenCalledWith(['/published', post.id], { state: { post } });
    });

});
