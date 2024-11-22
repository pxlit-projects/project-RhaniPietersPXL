package be.pxl.services.Domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewApprovalMessage  {
    private Long postId;
    private String state;
    private String rejectedMessage;
}
