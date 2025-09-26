package com.blogbaaz.SearchService.dtos;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchResponse {
    
    private String postId;
    private String title;
    private String excerpt;
    private String content;
    private String authorId;
    private String authorName;
    private String status;
    private Set<String> tags;
    private String categoryId;
    private String categoryName;
    private String slug;
    private String featuredImage;
    private boolean isPublished;
    private boolean isFeatured;
    private int viewCount;
    private int likeCount;
    private int commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;
    
    // Search-specific fields
    private double relevanceScore;
    private List<String> highlightedText;
}
