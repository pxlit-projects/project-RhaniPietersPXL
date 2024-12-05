package be.pxl.services.domain.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommentUpdateTest {

    @Test
    public void testCommentUpdate() {
        CommentUpdate commentUpdate = CommentUpdate.builder()
                .title("Updated Comment Title")
                .content("Updated content of the comment")
                .build();

        assertNotNull(commentUpdate);
        assertEquals("Updated Comment Title", commentUpdate.getTitle());
        assertEquals("Updated content of the comment", commentUpdate.getContent());
    }
}

