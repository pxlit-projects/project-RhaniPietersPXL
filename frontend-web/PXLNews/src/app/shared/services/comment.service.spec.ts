import { TestBed } from '@angular/core/testing';
import { CommentService } from './comment.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import { Comment } from '../models/comment.model';
import { environment } from '../../../environments/environment';

describe('CommentService', () => {
  let service: CommentService;
  let httpMock: HttpTestingController;

  const apiUrl = environment.apiUrlComment;

  const mockComment: Comment = {
    title: 'Test Title',
    content: 'Test content',
    author: 'John Doe',
    postId: 1
  };

  const mockComments: Comment[] = [
    { title: 'First comment', content: 'This is the first comment', author: 'John Doe', postId: 1 },
    { title: 'Second comment', content: 'This is the second comment', author: 'Jane Doe', postId: 1 }
  ];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [CommentService]
    });
    service = TestBed.inject(CommentService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should add a comment on post', () => {
    service.commentOnPost(mockComment).subscribe(response => {
      expect(response).toEqual(mockComment);
    });

    const req = httpMock.expectOne(`${apiUrl}/${mockComment.postId}`);
    expect(req.request.method).toBe('POST');
    req.flush(mockComment);
  });

  it('should get comments for a post', () => {
    service.getComments(1).subscribe(response => {
      expect(response).toEqual(mockComments);
    });

    const req = httpMock.expectOne(`${apiUrl}/1`);
    expect(req.request.method).toBe('GET');
    req.flush(mockComments);
  });

  it('should update a comment', () => {
    const updatedComment: Comment = { ...mockComment, id: 1, content: 'Updated comment content' };

    service.updateComment(updatedComment).subscribe(response => {
      expect(response).toEqual(updatedComment);
    });

    const req = httpMock.expectOne(`${apiUrl}/${updatedComment.id}`);
    expect(req.request.method).toBe('PUT');
    req.flush(updatedComment);
  });

  afterEach(() => {
    httpMock.verify();
  });
});
