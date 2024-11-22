package be.pxl.services.domain.dto;

import be.pxl.services.domain.State;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewApprovalResponse {
    private State state;
    private String rejectedMessage;
    private Long postId;
}
