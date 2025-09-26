package com.blogbaaz.SearchService.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchResult {
    
    private List<SearchResponse> posts;
    private int totalResults;
    private String searchQuery;
    private long searchTimeMs;
    private String sortBy;
    private String sortDirection;
}
