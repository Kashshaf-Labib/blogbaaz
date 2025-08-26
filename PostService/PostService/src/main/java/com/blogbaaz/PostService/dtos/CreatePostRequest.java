package com.blogbaaz.PostService.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePostRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 255, message = "Title must be between 5 and 255 characters")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    private String excerpt;

    @NotBlank(message = "Author ID is required")
    private String authorId;

    @NotBlank(message = "Author name is required")
    private String authorName;

    private Set<String> tags;

    private String categoryId;

    private String featuredImage;

    private boolean isFeatured;
}
