package be.pxl.services.domain.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommentResponseTest {

    @Test
    public void testCommentResponse() {
        CommentResponse commentResponse = CommentResponse.builder()
                .id(1L)
                .title("Response Title")
                .content("Response content")
                .author("Response Author")
                .postId(2L)
                .build();

        assertNotNull(commentResponse);
        assertEquals(1L, commentResponse.getId());
        assertEquals("Response Title", commentResponse.getTitle());
        assertEquals("Response content", commentResponse.getContent());
        assertEquals("Response Author", commentResponse.getAuthor());
        assertEquals(2L, commentResponse.getPostId());
    }
}
