package com.blogbaaz.CommentService.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCommentRequest {
    
    @NotBlank(message = "Post ID is required")
    private String postId;
    
    @NotBlank(message = "Content is required")
    @Size(min = 1, max = 2000, message = "Content must be between 1 and 2000 characters")
    private String content;
    
    private String parentCommentId; // Optional for replies
}
