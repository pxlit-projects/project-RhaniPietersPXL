package be.pxl.services.services;

import be.pxl.services.domain.dto.CommentRequest;
import be.pxl.services.domain.dto.CommentResponse;
import be.pxl.services.domain.dto.CommentUpdate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ICommentService {
    List<CommentResponse> getAllComments(Long id);

    CommentResponse updateComment(@PathVariable Long id, @RequestBody CommentUpdate commentUpdate);

    void deleteComment(Long id);

    CommentResponse createComment(Long postId, CommentRequest commentRequest);

    Integer getCommentCount(Long postId);
}


