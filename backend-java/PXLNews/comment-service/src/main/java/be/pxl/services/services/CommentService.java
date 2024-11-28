package be.pxl.services.services;

import be.pxl.services.domain.Comment;
import be.pxl.services.domain.dto.CommentRequest;
import be.pxl.services.domain.dto.CommentResponse;
import be.pxl.services.domain.dto.CommentUpdate;
import be.pxl.services.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {
    private final CommentRepository commentRepository;
    private static final Logger log = LoggerFactory.getLogger(CommentService.class);

    private CommentResponse mapToCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .title(comment.getTitle())
                .content(comment.getContent())
                .author(comment.getAuthor())
                .postId(comment.getPostId())
                .build();
    }

    @Override
    public List<CommentResponse> getAllComments(Long id) {
        log.info("Fetching all comments");
        return commentRepository.findByPostId(id).stream().map(this::mapToCommentResponse).toList();
    }

    @Override
    public CommentResponse updateComment(Long id, CommentUpdate commentUpdate) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        comment.setTitle(commentUpdate.getTitle());
        comment.setContent(commentUpdate.getContent());
        commentRepository.save(comment);
        return mapToCommentResponse(comment);
    }

    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public CommentResponse createComment(Long postId, CommentRequest commentRequest) {
        Comment comment = Comment.builder()
                .title(commentRequest.getTitle())
                .content(commentRequest.getContent())
                .author(commentRequest.getAuthor())
                .postId(postId)
                .build();
        log.info("Creating comment for post with auhor: {}", commentRequest.getAuthor());
        commentRepository.save(comment);
        return mapToCommentResponse(comment);
    }

    @Override
    public Integer getCommentCount(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return (comments == null) ? 0 : comments.size();
    }
}
