package hypermart.hypermart.comment.repository;

import hypermart.hypermart.comment.model.Comment;
import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.member.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface CommentRepositoryCustom {

    Comment findOneById(Long id);

    Comment findOneByWriter(Member writer);

    Page<Comment> findCommentsByItem(Item item, Pageable pageable);

    void editContent(String content, @Param("id") Long id);

    void deleteBulkCommentsByItem(Item item);
}
