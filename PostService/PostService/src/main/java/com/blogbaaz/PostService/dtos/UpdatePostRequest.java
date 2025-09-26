package com.blogbaaz.PostService.dtos;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePostRequest {

    @Size(min = 5, max = 255, message = "Title must be between 5 and 255 characters")
    private String title;

    private String content;
    private String excerpt;
    private Set<String> tags;
    private String category;
    private String featuredImage;
    private String status;
}
