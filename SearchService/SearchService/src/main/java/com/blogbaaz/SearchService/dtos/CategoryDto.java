package com.blogbaaz.SearchService.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {
    
    private String categoryId;
    private String name;
    private String description;
    private String slug;
    private String color;
    private boolean isActive;
    private LocalDateTime createdAt;
}
