package be.pxl.services.domain.dto;

import be.pxl.services.domain.Post;
import be.pxl.services.domain.State;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private Long id;
    private String rejectedMessage;
    private PostResponse post;
}
