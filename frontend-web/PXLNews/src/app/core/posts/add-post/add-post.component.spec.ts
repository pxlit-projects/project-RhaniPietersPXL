import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AddPostComponent } from './add-post.component';
import { PostService } from "../../../shared/services/post.service";
import { Router } from "@angular/router";
import { AuthService } from "../../../shared/services/auth.service";
import { FormsModule } from "@angular/forms";
import { of } from 'rxjs';

// Mock services
class MockPostService {
    addPost(post: any) {
        return of(true);
    }

    savePostAsDraft(post: any) {
        return of(true);
    }
}

class MockAuthService {
    getUsername() {
        return 'testUser';
    }
}

class MockRouter {
    navigate(path: string[]) {}
}

describe('AddPostComponent', () => {
    let component: AddPostComponent;
    let fixture: ComponentFixture<AddPostComponent>;
    let mockPostService: MockPostService;
    let mockAuthService: MockAuthService;
    let mockRouter: MockRouter;

    beforeEach(async () => {
        mockPostService = new MockPostService();
        mockAuthService = new MockAuthService();
        mockRouter = new MockRouter();

        await TestBed.configureTestingModule({
            imports: [AddPostComponent, FormsModule],
            providers: [
                { provide: PostService, useValue: mockPostService },
                { provide: AuthService, useValue: mockAuthService },
                { provide: Router, useValue: mockRouter }
            ]
        }).compileComponents();

        fixture = TestBed.createComponent(AddPostComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create the component', () => {
        expect(component).toBeTruthy();
    });

    it('should submit the post form', () => {
        spyOn(mockPostService, 'addPost').and.callThrough();
        spyOn(mockRouter, 'navigate');

        component.setAction('approval');
        component.postForm = {
            creationDate: '',
            state: '',
            title: 'Test Post',
            content: 'This is a test post.',
            category: 'Test Category',
            author: 'testUser'
        };

        component.onSubmit({});

        expect(mockPostService.addPost).toHaveBeenCalledWith({
            creationDate: jasmine.any(String),
            state: 'PENDING_APPROVAL',
            title: 'Test Post',
            content: 'This is a test post.',
            category: 'Test Category',
            author: 'testUser'
        });

        expect(mockRouter.navigate).toHaveBeenCalledWith(['/drafts']);
    });



    it('should reset the form', () => {
        component.postForm = {
            creationDate: '2024-11-28',
            state: 'DRAFT',
            title: 'Test Title',
            content: 'Test Content',
            category: 'Test Category',
            author: 'testUser'
        };

        component.resetForm();

        expect(component.postForm.title).toBe('');
        expect(component.postForm.content).toBe('');
        expect(component.postForm.category).toBe('');
        expect(component.postForm.state).toBe('');
    });
    it('should prompt for confirmation if form is not empty', () => {
        spyOn(window, 'confirm').and.returnValue(true);

        component.postForm.title = 'Test Title';
        const result = component.canDeactivate();

        expect(window.confirm).toHaveBeenCalledWith('Are you sure you want to leave this page?');
        expect(result).toBe(true);
    });

    it('should allow navigation if form is empty', () => {
        spyOn(window, 'confirm');

        component.postForm.title = '';
        const result = component.canDeactivate();

        expect(window.confirm).not.toHaveBeenCalled();
        expect(result).toBe(true);
    });

    it('should set the action correctly', () => {
        component.setAction('approval');
        expect(component.action).toBe('approval');

        component.setAction('draft');
        expect(component.action).toBe('draft');
    });


});
