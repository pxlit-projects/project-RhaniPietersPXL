import {CommentDetailComponent} from "./comment-detail.component";
import {ComponentFixture, TestBed} from "@angular/core/testing";
import {Post} from "../../../shared/models/post.model";
import {AuthService} from "../../../shared/services/auth.service";
import {CommentService} from "../../../shared/services/comment.service";
import {Comment} from "../../../shared/models/comment.model";
import {of} from "rxjs";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {NgClass} from "@angular/common";
import {CommentFormComponent} from "../comment-form/comment-form.component";
import {CommentComponent} from "../comment/comment.component";
import {PostItemComponent} from "../../posts/post-item/post-item.component";
import {DetailComponent} from "../../posts/detail/detail.component";
import {Router} from "@angular/router";


describe('CommentDetailComponent', () => {
    let component: CommentDetailComponent;
    let fixture: ComponentFixture<CommentDetailComponent>;
    let commentServiceMock: jasmine.SpyObj<CommentService>;
    let authServiceMock: jasmine.SpyObj<AuthService>;

    let post: Post;
    let comments: Comment[];

    beforeEach(() => {
        // Mock services
        commentServiceMock = jasmine.createSpyObj('CommentService', ['getComments', 'commentOnPost']);
        authServiceMock = jasmine.createSpyObj('AuthService', ['getUsername']);

        // Mock data
        post = new Post('Title 1', 'Content 1', 'Author 1', '2023-11-28', 'Category 1', 'published');
        comments = [
            new Comment('Comment 1', 'Comment Content 1', 'User 1', 1),
            new Comment('Comment 2', 'Comment Content 2', 'User 2', 1)
        ];

        // Set up the mocks
        authServiceMock.getUsername.and.returnValue('testUser');
        commentServiceMock.getComments.and.returnValue(of(comments));
        commentServiceMock.commentOnPost.and.returnValue(of(comments[0]));

        spyOnProperty(window, 'history', 'get').and.returnValue({
            ...window.history,
            state: {post: post}
        });

        TestBed.configureTestingModule({
            imports: [ReactiveFormsModule, FormsModule, NgClass, CommentFormComponent, CommentComponent, PostItemComponent, DetailComponent, CommentDetailComponent],
            providers: [
                {provide: CommentService, useValue: commentServiceMock},
                {provide: AuthService, useValue: authServiceMock},
                {provide: Router, useValue: {}}
            ]
        }).compileComponents();

        fixture = TestBed.createComponent(CommentDetailComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create the component', () => {
        expect(component).toBeTruthy();
    });

    it('should load comments on init', () => {
        // Act
        component.ngOnInit();

        // Assert
        expect(commentServiceMock.getComments).toHaveBeenCalledWith(post.id!);
        expect(component.comments).toEqual(comments);
    });

    it('should toggle the comment input visibility', () => {
        // Arrange
        expect(component.showCommentInput).toBeFalse();

        // Act
        component.toggleAddComment();

        // Assert
        expect(component.showCommentInput).toBeTrue();

        // Act again
        component.toggleAddComment();

        // Assert again
        expect(component.showCommentInput).toBeFalse();
    });

    it('should delete a comment', () => {
        // Arrange
        const commentToDelete = comments[0];

        // Act
        component.onDelete(commentToDelete);

        // Assert
        expect(component.comments).not.toContain(commentToDelete);
    });

    it('should edit a comment', () => {
        // Arrange
        const updatedComment = new Comment('Updated Comment', 'Updated Content', 'User 1', 1);
        component.comments = [comments[0]];

        // Act
        component.onEdit(updatedComment);

        // Assert
        expect(component.comments[0]).toEqual(updatedComment);
    });

    it('should prompt for confirmation if form is dirty in canDeactivate', () => {
        // Arrange
        const confirmSpy = spyOn(window, 'confirm').and.returnValue(true);
        component.formComponent = {commentForm: {dirty: true}} as any;

        // Act
        const result = component.canDeactivate();

        // Assert
        expect(confirmSpy).toHaveBeenCalledWith('Are you sure you want to leave this page?');
        expect(result).toBe(true); // Assuming the user confirms
    });

    it('should not prompt for confirmation if form is clean in canDeactivate', () => {
        // Arrange
        const confirmSpy = spyOn(window, 'confirm');
        component.formComponent = {commentForm: {dirty: false}} as any;

        // Act
        const result = component.canDeactivate();

        // Assert
        expect(confirmSpy).not.toHaveBeenCalled();
        expect(result).toBe(true);
    });

    it('should set isUserLoggedIn to true when the user is logged in', () => {
        // Assert
        expect(component.isUserLoggedIn).toBeTrue();
    });
});
