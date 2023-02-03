package hypermart.hypermart.comment.util;

import hypermart.hypermart.comment.dto.CommentRequest;
import hypermart.hypermart.comment.dto.CommentResponse;
import hypermart.hypermart.comment.dto.CommentsPageResponse;
import hypermart.hypermart.comment.model.Comment;
import org.springframework.data.domain.Page;

public class CommentMapper {

    public static Comment dtoToEntity(CommentRequest commentRequest) {
        return Comment.builder()
                .id(commentRequest.getId())
                .content(commentRequest.getContent())
                .writer(commentRequest.getWriter())
                .item(commentRequest.getItem())
                .build();
    }

    private static CommentResponse dtoBuilder(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .writer(comment.getWriter().getEmail())
                .createdDate(comment.getCreatedDate())
                .build();
    }

    public static Page<CommentResponse> entityToDtoPage(Page<Comment> comments) {
        return comments.map(CommentMapper::dtoBuilder);
    }

    public static CommentResponse entityToDtoDetail(Comment comment) {
        return CommentMapper.dtoBuilder(comment);
    }

    public static CommentsPageResponse createCommentsPageResponse(
            Page<CommentResponse> comments,
            String user
    ) {
        return CommentsPageResponse.builder()
                .comments(comments)
                .user(user)
                .build();
    }
}
