package hypermart.hypermart.comment.repository;

import hypermart.hypermart.comment.model.Comment;
import hypermart.hypermart.item.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c join c.writer join c.item where c.item = :item")
    Page<Comment> findCommentsByItem(@Param("item") Item item, Pageable pageable);

    @Modifying
    @Query("update Comment c set c.content = :content where c.id = :id")
    void editContent(@Param("content") String content, @Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query("delete from Comment c where c.item = :item")
    void deleteBulkCommentsByItem(@Param("item") Item item);
}
