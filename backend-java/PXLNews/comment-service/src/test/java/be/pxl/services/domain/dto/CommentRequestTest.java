package be.pxl.services.domain.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommentRequestTest {

    @Test
    public void testCommentRequest() {
        CommentRequest commentRequest = CommentRequest.builder()
                .title("New Comment")
                .content("This is the content of the comment")
                .author("Author Name")
                .postId(3L)
                .build();

        assertNotNull(commentRequest);
        assertEquals("New Comment", commentRequest.getTitle());
        assertEquals("This is the content of the comment", commentRequest.getContent());
        assertEquals("Author Name", commentRequest.getAuthor());
        assertEquals(3L, commentRequest.getPostId());
    }
}
