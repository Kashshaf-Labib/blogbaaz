package com.blogbaaz.CommentService.services.impl;

import com.blogbaaz.CommentService.dtos.*;
import com.blogbaaz.CommentService.entities.Comment;
import com.blogbaaz.CommentService.exceptions.CommentNotFoundException;
import com.blogbaaz.CommentService.exceptions.UnauthorizedException;
import com.blogbaaz.CommentService.repositories.CommentRepository;
import com.blogbaaz.CommentService.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public CommentDto createComment(CreateCommentRequest request, String authorId, String authorName) {
        Comment comment = Comment.builder()
                .postId(request.getPostId())
                .authorId(authorId)
                .authorName(authorName)
                .content(request.getContent())
                .parentCommentId(request.getParentCommentId())
                .level(0)
                .path("temp") // Temporary value
                .build();

        // Save the comment first to get the generated ID
        Comment savedComment = commentRepository.save(comment);

        // Now set up the hierarchy with the actual comment ID
        if (request.getParentCommentId() != null && !request.getParentCommentId().isEmpty()) {
            Comment parentComment = commentRepository.findByCommentIdAndIsDeletedFalse(request.getParentCommentId())
                    .orElseThrow(() -> new CommentNotFoundException("Parent comment not found"));
            
            savedComment.setLevel(parentComment.getLevel() + 1);
            savedComment.setPath(parentComment.getPath() + "/" + savedComment.getCommentId());
            
            // Update parent's reply count
            parentComment.setReplyCount(parentComment.getReplyCount() + 1);
            commentRepository.save(parentComment);
        } else {
            // Root comment - set path to its own ID
            savedComment.setPath(savedComment.getCommentId());
        }

        // Save the comment again with the updated path
        Comment finalSavedComment = commentRepository.save(savedComment);
        return entityToDto(finalSavedComment);
    }

    @Override
    public CommentDto getCommentById(String commentId) {
        Comment comment = commentRepository.findByCommentIdAndIsDeletedFalse(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + commentId));
        return entityToDto(comment);
    }

    @Override
    public CommentDto updateComment(String commentId, UpdateCommentRequest request, String authorId) {
        Comment comment = commentRepository.findByCommentIdAndIsDeletedFalse(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + commentId));

        // Check if user is the author
        if (!comment.getAuthorId().equals(authorId)) {
            throw new UnauthorizedException("You are not authorized to update this comment");
        }

        comment.setContent(request.getContent());
        Comment updatedComment = commentRepository.save(comment);
        return entityToDto(updatedComment);
    }

    @Override
    public void deleteComment(String commentId, String authorId) {
        Comment comment = commentRepository.findByCommentIdAndIsDeletedFalse(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + commentId));

        // Check if user is the author
        if (!comment.getAuthorId().equals(authorId)) {
            throw new UnauthorizedException("You are not authorized to delete this comment");
        }

        // Soft delete
        comment.setDeleted(true);
        commentRepository.save(comment);

        // Update parent's reply count if this is a reply
        if (comment.getParentCommentId() != null && !comment.getParentCommentId().isEmpty()) {
            Comment parentComment = commentRepository.findByCommentIdAndIsDeletedFalse(comment.getParentCommentId())
                    .orElse(null);
            if (parentComment != null) {
                parentComment.setReplyCount(Math.max(0, parentComment.getReplyCount() - 1));
                commentRepository.save(parentComment);
            }
        }
    }

    @Override
    public List<CommentTreeResponse> getCommentsByPostId(String postId) {
        List<Comment> comments = commentRepository.findCommentTreeByPostId(postId);
        return buildCommentTree(comments);
    }

    @Override
    public List<CommentTreeResponse> getRootCommentsByPostId(String postId) {
        List<Comment> rootComments = commentRepository.findByPostIdAndParentCommentIdIsNullAndIsDeletedFalseOrderByCreatedAtAsc(postId);
        return rootComments.stream()
                .map(this::entityToTreeResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getRepliesByCommentId(String commentId) {
        List<Comment> replies = commentRepository.findByParentCommentIdAndIsDeletedFalseOrderByCreatedAtAsc(commentId);
        return replies.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getCommentsByAuthor(String authorId) {
        List<Comment> comments = commentRepository.findByAuthorIdAndIsDeletedFalseOrderByCreatedAtDesc(authorId);
        return comments.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<CommentDto> getCommentsByPostIdPaginated(String postId, Pageable pageable) {
        return commentRepository.findByPostIdAndIsDeletedFalseOrderByCreatedAtDesc(postId, pageable)
                .map(this::entityToDto);
    }

    @Override
    public Page<CommentDto> getRootCommentsByPostIdPaginated(String postId, Pageable pageable) {
        return commentRepository.findByPostIdAndParentCommentIdIsNullAndIsDeletedFalseOrderByCreatedAtDesc(postId, pageable)
                .map(this::entityToDto);
    }

    @Override
    public void likeComment(String commentId) {
        Comment comment = commentRepository.findByCommentIdAndIsDeletedFalse(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + commentId));
        comment.setLikeCount(comment.getLikeCount() + 1);
        commentRepository.save(comment);
    }

    @Override
    public void unlikeComment(String commentId) {
        Comment comment = commentRepository.findByCommentIdAndIsDeletedFalse(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + commentId));
        comment.setLikeCount(Math.max(0, comment.getLikeCount() - 1));
        commentRepository.save(comment);
    }

    @Override
    public long getCommentCountByPostId(String postId) {
        return commentRepository.countByPostIdAndIsDeletedFalse(postId);
    }

    @Override
    public long getReplyCountByCommentId(String commentId) {
        return commentRepository.countByParentCommentIdAndIsDeletedFalse(commentId);
    }

    @Override
    public List<CommentDto> searchComments(String keyword) {
        List<Comment> comments = commentRepository.searchCommentsByContent(keyword);
        return comments.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isCommentOwner(String commentId, String authorId) {
        Comment comment = commentRepository.findByCommentIdAndIsDeletedFalse(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + commentId));
        return comment.getAuthorId().equals(authorId);
    }

    // Helper methods
    private CommentDto entityToDto(Comment comment) {
        return CommentDto.builder()
                .commentId(comment.getCommentId())
                .postId(comment.getPostId())
                .authorId(comment.getAuthorId())
                .authorName(comment.getAuthorName())
                .content(comment.getContent())
                .parentCommentId(comment.getParentCommentId())
                .level(comment.getLevel())
                .path(comment.getPath())
                .isDeleted(comment.isDeleted())
                .likeCount(comment.getLikeCount())
                .replyCount(comment.getReplyCount())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    private CommentTreeResponse entityToTreeResponse(Comment comment) {
        return CommentTreeResponse.builder()
                .commentId(comment.getCommentId())
                .postId(comment.getPostId())
                .authorId(comment.getAuthorId())
                .authorName(comment.getAuthorName())
                .content(comment.getContent())
                .parentCommentId(comment.getParentCommentId())
                .level(comment.getLevel())
                .isDeleted(comment.isDeleted())
                .likeCount(comment.getLikeCount())
                .replyCount(comment.getReplyCount())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    private List<CommentTreeResponse> buildCommentTree(List<Comment> comments) {
        // This is a simplified tree building - in a real implementation,
        // you might want to use a more sophisticated algorithm
        return comments.stream()
                .filter(comment -> comment.getParentCommentId() == null || comment.getParentCommentId().isEmpty())
                .map(this::buildCommentTreeRecursive)
                .collect(Collectors.toList());
    }

    private CommentTreeResponse buildCommentTreeRecursive(Comment comment) {
        CommentTreeResponse response = entityToTreeResponse(comment);
        
        // Find and add replies
        List<Comment> replies = commentRepository.findByParentCommentIdAndIsDeletedFalseOrderByCreatedAtAsc(comment.getCommentId());
        List<CommentTreeResponse> replyResponses = replies.stream()
                .map(this::buildCommentTreeRecursive)
                .collect(Collectors.toList());
        
        response.setReplies(replyResponses);
        return response;
    }
}
