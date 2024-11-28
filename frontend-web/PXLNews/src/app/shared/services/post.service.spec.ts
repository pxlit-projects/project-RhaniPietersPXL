import { TestBed } from '@angular/core/testing';
import { PostService } from './post.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Post } from '../models/post.model';
import { Filter } from '../models/filter.model';
import { environment } from '../../../environments/environment';

describe('PostService', () => {
  let service: PostService;
  let httpMock: HttpTestingController;

  const apiUrl = environment.apiUrlPost;

  const mockPost: Post = {
    id: 1,
    title: 'Test Post',
    content: 'This is a test post',
    author: 'John Doe',
    creationDate: '2024-11-28',
    category: 'Test Category',
    state: 'published',
  };

  const mockPosts: Post[] = [
    { id: 1, title: 'Test Post 1', content: 'Content 1', author: 'John Doe', creationDate: '2024-11-28', category: 'Category 1', state: 'published' },
    { id: 2, title: 'Test Post 2', content: 'Content 2', author: 'Jane Doe', creationDate: '2024-11-27', category: 'Category 2', state: 'draft' }
  ];

  const mockFilter: Filter = {
    content: 'Test',
    author: 'John',
    category: 'Category 1'
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [PostService]
    });
    service = TestBed.inject(PostService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get published posts', () => {
    service.getPublishedPosts().subscribe(posts => {
      expect(posts).toEqual(mockPosts);
    });

    const req = httpMock.expectOne(apiUrl);
    expect(req.request.method).toBe('GET');
    req.flush(mockPosts);
  });

  it('should get drafts for a specific author', () => {
    const author = 'John Doe';
    service.getDrafts(author).subscribe(posts => {
      expect(posts).toEqual([mockPosts[0]]);
    });

    const req = httpMock.expectOne(`${apiUrl}/drafts/${author}`);
    expect(req.request.method).toBe('GET');
    req.flush([mockPosts[0]]);
  });

  it('should add a post', () => {
    service.addPost(mockPost).subscribe(post => {
      expect(post).toEqual(mockPost);
    });

    const req = httpMock.expectOne(apiUrl);
    expect(req.request.method).toBe('POST');
    req.flush(mockPost);
  });

  it('should save a post as draft', () => {
    service.savePostAsDraft(mockPost).subscribe(post => {
      expect(post).toEqual(mockPost);
    });

    const req = httpMock.expectOne(apiUrl);
    expect(req.request.method).toBe('POST');
    req.flush(mockPost);
  });

  it('should update a post', () => {
    const updatedPost: Post = { ...mockPost, content: 'Updated content' };

    service.updatePost(updatedPost).subscribe(post => {
      expect(post).toEqual(updatedPost);
    });

    const req = httpMock.expectOne(`${apiUrl}/${updatedPost.id}`);
    expect(req.request.method).toBe('PUT');
    req.flush(updatedPost);
  });

  it('should ask for approval of a post', () => {
    const postId = 1;

    service.askApproval(postId).subscribe(post => {
      expect(post).toEqual(mockPost);
    });

    const req = httpMock.expectOne(`${apiUrl}/${postId}/approval`);
    expect(req.request.method).toBe('POST');
    req.flush(mockPost);
  });

  it('should publish a post', () => {
    const postId = 1;

    service.publishPost(postId).subscribe(post => {
      expect(post).toEqual(mockPost);
    });

    const req = httpMock.expectOne(`${apiUrl}/${postId}/publish`);
    expect(req.request.method).toBe('POST');
    req.flush(mockPost);
  });

  afterEach(() => {
    httpMock.verify();
  });
});
