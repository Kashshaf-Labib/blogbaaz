package com.blogbaaz.CommentService.dtos;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentTreeResponse {
    private String commentId;
    private String postId;
    private String authorId;
    private String authorName;
    private String content;
    private String parentCommentId;
    private int level;
    private boolean isDeleted;
    private int likeCount;
    private int replyCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CommentTreeResponse> replies;
}
