package be.pxl.services.domain.dto;

import be.pxl.services.domain.Category;
import be.pxl.services.domain.State;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime creationDate;
    private Category category;
    private State state;
    private String rejectedMessage;
    private Integer commentCount;
}