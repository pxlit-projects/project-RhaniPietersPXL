import {ReactiveFormsModule} from "@angular/forms";
import {ComponentFixture, TestBed} from "@angular/core/testing";
import {Comment} from "../../../shared/models/comment.model";
import {CommentComponent} from "./comment.component";
import {CommentService} from "../../../shared/services/comment.service";
import {NgClass} from "@angular/common";
import {AuthService} from "../../../shared/services/auth.service";
import {CommentFormComponent} from "../comment-form/comment-form.component";
import {Router} from "@angular/router";
import {of} from "rxjs";


describe('CommentComponent', () => {
    let component: CommentComponent;
    let fixture: ComponentFixture<CommentComponent>;
    let commentServiceMock: jasmine.SpyObj<CommentService>;
    let authServiceMock: jasmine.SpyObj<AuthService>;

    let comment: Comment;

    beforeEach(() => {
        commentServiceMock = jasmine.createSpyObj('CommentService', ['deleteComment', 'updateComment']);
        comment = new Comment('Test Title', 'Test Content', 'Author', 1);
        authServiceMock = jasmine.createSpyObj('AuthService', ['getUsername']);
        authServiceMock.getUsername.and.returnValue('testUser');

        TestBed.configureTestingModule({
            imports: [ReactiveFormsModule, NgClass, CommentFormComponent, CommentComponent],
            providers: [
                {provide: CommentService, useValue: commentServiceMock},
                {provide: AuthService, useValue: authServiceMock},
                {provide: Router, useValue: {}},
            ]
        }).compileComponents();

        fixture = TestBed.createComponent(CommentComponent);
        component = fixture.componentInstance;
        component.comment = comment;
        component.postId = 1;
        fixture.detectChanges();
    });

    it('should create the component', () => {
        expect(component).toBeTruthy();
    });

    it('should call deleteComment and emit onCommentDeleted', () => {
        // Arrange
        const commentDeleteSpy = spyOn(component.onCommentDeleted, 'emit');
        commentServiceMock.deleteComment.and.returnValue(of(comment));

        // Act
        component.onDelete();

        // Assert
        expect(commentServiceMock.deleteComment).toHaveBeenCalledWith(comment.id!);
        expect(commentDeleteSpy).toHaveBeenCalledWith(comment);
    });

    it('should call updateComment and emit onCommentEdit', () => {
        // Arrange
        const updatedComment = new Comment('Updated Title', 'Updated Content', 'Author', 1);
        const commentEditSpy = spyOn(component.onCommentEdit, 'emit');
        commentServiceMock.updateComment.and.returnValue(of(updatedComment));

        // Act
        component.onSubmitEdit(updatedComment);

        // Assert
        expect(commentServiceMock.updateComment).toHaveBeenCalledWith(updatedComment);
        expect(commentEditSpy).toHaveBeenCalledWith(updatedComment);
    });

    it('should cancel edit and set isEditing to false', () => {
        // Arrange
        component.isEditing = true;

        // Act
        component.onCancelEdit();

        // Assert
        expect(component.isEditing).toBe(false);
    });

    it('should return true for canDeactivate when form is not dirty', () => {
        // Arrange
        const canDeactivate = component.canDeactivate();

        // Act & Assert
        expect(canDeactivate).toBe(true);
    });

    it('should prompt for confirmation when form is dirty in canDeactivate', () => {
        // Arrange
        const confirmSpy = spyOn(window, 'confirm').and.returnValue(true);
        const dirtyFormComponent = {commentForm: {dirty: true}};
        component.formComponents = {toArray: () => [dirtyFormComponent]} as any;

        // Act
        const canDeactivate = component.canDeactivate();

        // Assert
        expect(confirmSpy).toHaveBeenCalledWith('Are you sure you want to leave this page?');
        expect(canDeactivate).toBe(true);
    });

    it('should not prompt for confirmation when form is not dirty in canDeactivate', () => {
        // Arrange
        const confirmSpy = spyOn(window, 'confirm');
        const cleanFormComponent = {commentForm: {dirty: false}};
        component.formComponents = {toArray: () => [cleanFormComponent]} as any;

        // Act
        const canDeactivate = component.canDeactivate();

        // Assert
        expect(confirmSpy).not.toHaveBeenCalled();
        expect(canDeactivate).toBe(true);
    });
});
