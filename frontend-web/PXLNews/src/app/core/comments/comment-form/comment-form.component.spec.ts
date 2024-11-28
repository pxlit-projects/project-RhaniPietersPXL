import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CommentFormComponent } from './comment-form.component';
import { ReactiveFormsModule } from '@angular/forms';
import { Comment } from '../../../shared/models/comment.model';
import { By } from '@angular/platform-browser';

describe('CommentFormComponent', () => {
  let component: CommentFormComponent;
  let fixture: ComponentFixture<CommentFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, CommentFormComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(CommentFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form with provided comment data when comment is passed', () => {
    // Arrange
    const comment: Comment = {
      title: 'Test Title',
      content: 'Test Content',
      author: 'Test User',
      postId: 1,
    };

    component.comment = comment;
    component.ngOnInit();

    fixture.detectChanges();

    // Assert
    expect(component.commentForm.value.title).toBe(comment.title);
    expect(component.commentForm.value.content).toBe(comment.content);
  });


  it('should submit an edited comment when form is valid and a comment exists', () => {
    // Arrange
    const existingComment: Comment = { title: 'Existing Title', content: 'Existing Content', author: 'Test User', postId: 1 };
    component.comment = existingComment;
    component.commentForm.setValue({ title: 'Updated Title', content: 'Updated Content' });
    const submitSpy = spyOn(component.submitComment, 'emit');
    fixture.detectChanges();

    // Act
    component.onSubmit();

    // Assert
    expect(submitSpy).toHaveBeenCalledWith({
      ...existingComment,
      title: 'Updated Title',
      content: 'Updated Content',
    });
  });

  it('should reset the form and emit cancel event when cancel is clicked', () => {
    // Arrange
    component.commentForm.setValue({ title: 'Test Title', content: 'Test Content' });
    const cancelSpy = spyOn(component.cancelEdit, 'emit');
    fixture.detectChanges();

    // Act
    component.cancel();

    // Assert
    expect(cancelSpy).toHaveBeenCalled();
    expect(component.commentForm.value).toEqual({ title: null, content: null });
  });


  it('should prompt the user when the form is dirty in canDeactivate', () => {
    // Arrange
    const confirmSpy = spyOn(window, 'confirm').and.returnValue(true);
    component.commentForm.setValue({ title: 'Test Title', content: 'Test Content' });
    component.commentForm.markAsDirty();
    fixture.detectChanges();

    // Act
    const result = component.canDeactivate();

    // Assert
    expect(confirmSpy).toHaveBeenCalledWith('Are you sure you want to leave this page?');
    expect(result).toBeTrue();
  });

  it('should not prompt the user when the form is clean in canDeactivate', () => {
    // Arrange
    const confirmSpy = spyOn(window, 'confirm');
    component.commentForm.setValue({ title: '', content: '' });
    component.commentForm.markAsPristine();
    fixture.detectChanges();

    // Act
    const result = component.canDeactivate();

    // Assert
    expect(confirmSpy).not.toHaveBeenCalled();
    expect(result).toBeTrue();
  });

  it('should call onSubmit when the submit button is clicked', () => {
    // Arrange
    const submitButton = fixture.debugElement.query(By.css('button[type="submit"]'));
    component.commentForm.setValue({ title: 'Test Title', content: 'Test Content' });
    const submitSpy = spyOn(component, 'onSubmit');
    fixture.detectChanges();

    // Act
    submitButton.nativeElement.click();

    // Assert
    expect(submitSpy).toHaveBeenCalled();
  });

  it('should call cancel when the cancel button is clicked', () => {
    // Arrange
    const cancelButton = fixture.debugElement.query(By.css('button[type="button"]'));
    const cancelSpy = spyOn(component, 'cancel');
    fixture.detectChanges();

    // Act
    cancelButton.triggerEventHandler('click', null);

    // Assert
    expect(cancelSpy).toHaveBeenCalled();
  });
});
