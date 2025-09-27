package com.blogbaaz.SearchService.dtos;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {
    private String postId;
    private String title;
    private String content;
    private String excerpt;
    private String authorId;
    private String authorName;
    private String status;
    private Set<String> tags;
    private String category;
    private String slug;
    private String featuredImage;
    private boolean isPublished;
    private int viewCount;
    private int likeCount;
    private int commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;
}
