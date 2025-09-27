package com.blogbaaz.CommentService.controllers;

import com.blogbaaz.CommentService.dtos.*;
import com.blogbaaz.CommentService.services.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDto> createComment(
            @Valid @RequestBody CreateCommentRequest request,
            HttpServletRequest httpRequest) {
        
        String userId = (String) httpRequest.getAttribute("userId");
        String authorName = "User"; // You might want to fetch this from UserService
        
        CommentDto createdComment = commentService.createComment(request, userId, authorName);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable String commentId) {
        CommentDto comment = commentService.getCommentById(commentId);
        return ResponseEntity.ok(comment);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable String commentId,
            @Valid @RequestBody UpdateCommentRequest request,
            HttpServletRequest httpRequest) {
        
        String userId = (String) httpRequest.getAttribute("userId");
        CommentDto updatedComment = commentService.updateComment(commentId, request, userId);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable String commentId,
            HttpServletRequest httpRequest) {
        
        String userId = (String) httpRequest.getAttribute("userId");
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentTreeResponse>> getCommentsByPostId(@PathVariable String postId) {
        List<CommentTreeResponse> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/post/{postId}/root")
    public ResponseEntity<List<CommentTreeResponse>> getRootCommentsByPostId(@PathVariable String postId) {
        List<CommentTreeResponse> comments = commentService.getRootCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{commentId}/replies")
    public ResponseEntity<List<CommentDto>> getRepliesByCommentId(@PathVariable String commentId) {
        List<CommentDto> replies = commentService.getRepliesByCommentId(commentId);
        return ResponseEntity.ok(replies);
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<CommentDto>> getCommentsByAuthor(@PathVariable String authorId) {
        List<CommentDto> comments = commentService.getCommentsByAuthor(authorId);
        return ResponseEntity.ok(comments);
    }

    // Paginated endpoints
    @GetMapping("/post/{postId}/paginated")
    public ResponseEntity<Page<CommentDto>> getCommentsByPostIdPaginated(
            @PathVariable String postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CommentDto> comments = commentService.getCommentsByPostIdPaginated(postId, pageable);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/post/{postId}/root/paginated")
    public ResponseEntity<Page<CommentDto>> getRootCommentsByPostIdPaginated(
            @PathVariable String postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<CommentDto> comments = commentService.getRootCommentsByPostIdPaginated(postId, pageable);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<Void> likeComment(@PathVariable String commentId) {
        commentService.likeComment(commentId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{commentId}/unlike")
    public ResponseEntity<Void> unlikeComment(@PathVariable String commentId) {
        commentService.unlikeComment(commentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/post/{postId}/count")
    public ResponseEntity<Long> getCommentCountByPostId(@PathVariable String postId) {
        long count = commentService.getCommentCountByPostId(postId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{commentId}/reply-count")
    public ResponseEntity<Long> getReplyCountByCommentId(@PathVariable String commentId) {
        long count = commentService.getReplyCountByCommentId(commentId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CommentDto>> searchComments(@RequestParam String keyword) {
        List<CommentDto> comments = commentService.searchComments(keyword);
        return ResponseEntity.ok(comments);
    }
}
