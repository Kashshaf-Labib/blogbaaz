package com.blogbaaz.SearchService.clients;

import com.blogbaaz.SearchService.dtos.CategoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "CATEGORY-SERVICE", url = "${category-service.url}")
public interface CategoryServiceClient {
    
    @GetMapping("/api/categories")
    List<CategoryDto> getAllCategories();
    
    @GetMapping("/api/categories/{categoryId}")
    CategoryDto getCategoryById(@PathVariable String categoryId);
    
    @GetMapping("/api/categories/search")
    List<CategoryDto> searchCategories(@RequestParam String query);
}
