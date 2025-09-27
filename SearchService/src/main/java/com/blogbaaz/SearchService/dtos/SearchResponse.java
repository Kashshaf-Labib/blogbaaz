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
    
    // Search-specific fields
    private double relevanceScore;
    private List<String> highlightedText;
    
    // Convert from PostDto
    public static SearchResponse fromPostDto(PostDto postDto) {
        return SearchResponse.builder()
                .postId(postDto.getPostId())
                .title(postDto.getTitle())
                .excerpt(postDto.getExcerpt())
                .content(postDto.getContent())
                .authorId(postDto.getAuthorId())
                .authorName(postDto.getAuthorName())
                .status(postDto.getStatus())
                .tags(postDto.getTags())
                .category(postDto.getCategory())
                .slug(postDto.getSlug())
                .featuredImage(postDto.getFeaturedImage())
                .isPublished(postDto.isPublished())
                .viewCount(postDto.getViewCount())
                .likeCount(postDto.getLikeCount())
                .commentCount(postDto.getCommentCount())
                .createdAt(postDto.getCreatedAt())
                .updatedAt(postDto.getUpdatedAt())
                .publishedAt(postDto.getPublishedAt())
                .relevanceScore(0.0) // Default relevance score
                .build();
    }
}
