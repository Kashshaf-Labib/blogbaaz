package com.blogbaaz.CommentService.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String commentId;

    @Column(nullable = false)
    private String postId;

    @Column(nullable = false)
    private String authorId;

    @Column(nullable = false)
    private String authorName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "parent_comment_id")
    private String parentCommentId;

    @Column(nullable = false)
    private int level = 0;

    @Column(nullable = false)
    private String path;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Column(nullable = false)
    private int likeCount = 0;

    @Column(nullable = false)
    private int replyCount = 0;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Self-referencing relationship for replies
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id", insertable = false, updatable = false)
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> replies = new ArrayList<>();

    // Helper methods
    public void addReply(Comment reply) {
        replies.add(reply);
        reply.setParentComment(this);
        reply.setLevel(this.level + 1);
        reply.setPath(this.path + "/" + reply.getCommentId());
        this.replyCount++;
    }

    public void removeReply(Comment reply) {
        replies.remove(reply);
        reply.setParentComment(null);
        this.replyCount--;
    }

    public boolean isRootComment() {
        return parentCommentId == null || parentCommentId.isEmpty();
    }

    public boolean hasReplies() {
        return !replies.isEmpty();
    }
}