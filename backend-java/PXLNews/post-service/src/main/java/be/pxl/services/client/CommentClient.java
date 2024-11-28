package be.pxl.services.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "comment-service")
public interface CommentClient {

    @GetMapping("/comment/count/{postId}")
    int getCommentCountForPost(@PathVariable("postId") Long postId);
}
