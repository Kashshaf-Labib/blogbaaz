package com.blogbaaz.SearchService.dtos;

import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Max;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {
    
    @NotBlank(message = "Search query is required")
    private String query;
    
    private String authorId;
    
    private String[] tags;
    
    @Max(value = 100, message = "Max results cannot exceed 100")
    private int maxResults = 20;
    
    private String sortBy = "createdAt";
    
    private String sortDirection = "desc";
    
    private boolean publishedOnly = true;
    
    private boolean featuredOnly = false;
}
