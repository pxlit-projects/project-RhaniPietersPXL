import { TestBed } from '@angular/core/testing';
import { ReviewService } from './review.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Review } from '../models/review.model';
import { Post } from '../models/post.model';
import { environment } from '../../../environments/environment';

describe('ReviewService', () => {
  let service: ReviewService;
  let httpMock: HttpTestingController;

  const apiUrl = environment.apiUrlReview;

  const mockPost: Post = {
    id: 1,
    title: 'Test Post',
    content: 'This is a test post content.',
    author: 'John Doe',
    creationDate: '2024-01-01',
    category: 'Technology',
    state: 'draft'
  };

  const mockReview: Review = {
    id: 1,
    rejectedMessage: 'Not appropriate for the platform',
    post: mockPost
  };

  const mockReviews: Review[] = [
    {
      id: 1,
      rejectedMessage: 'Post does not meet guidelines',
      post: { ...mockPost, id: 1 }
    },
    {
      id: 2,
      rejectedMessage: 'Post lacks proper structure',
      post: { ...mockPost, id: 2 }
    }
  ];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ReviewService]
    });
    service = TestBed.inject(ReviewService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get posts to approve', () => {
    const author = 'John Doe';

    service.getPostsToApprove(author).subscribe(reviews => {
      expect(reviews).toEqual(mockReviews);
    });

    const req = httpMock.expectOne(`${apiUrl}/approval/${author}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockReviews);
  });

  it('should approve a post', () => {
    const postId = 1;

    service.approvePost(postId).subscribe(response => {
      expect(response).toEqual(mockReview);
    });

    const req = httpMock.expectOne(`${apiUrl}/${postId}/approve`);
    expect(req.request.method).toBe('POST');
    req.flush(mockReview);
  });

  it('should reject a post', () => {
    const postId = 1;
    const rejectMessage = 'Post not suitable for approval';

    service.rejectPost(postId, rejectMessage).subscribe(response => {
      expect(response).toEqual(mockReview);
    });

    const req = httpMock.expectOne(`${apiUrl}/${postId}/reject`);
    expect(req.request.method).toBe('POST');
    req.flush(mockReview);
  });

  afterEach(() => {
    httpMock.verify();
  });
});
