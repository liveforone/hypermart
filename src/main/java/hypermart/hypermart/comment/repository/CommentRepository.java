package hypermart.hypermart.comment.repository;

import hypermart.hypermart.comment.model.Comment;
import hypermart.hypermart.item.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c join c.writer join c.item where c.item = :item")
    Page<Comment> findCommentsByItem(@Param("item") Item item, Pageable pageable);
}
