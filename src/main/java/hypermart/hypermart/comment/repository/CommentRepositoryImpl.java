package hypermart.hypermart.comment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hypermart.hypermart.comment.model.Comment;
import hypermart.hypermart.comment.model.QComment;
import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.member.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public Comment findOneById(Long id) {
        QComment comment = QComment.comment;

        return queryFactory.selectFrom(comment)
                .join(comment.writer).fetchJoin()
                .join(comment.item).fetchJoin()
                .where(comment.id.eq(id))
                .fetchOne();
    }

    public Comment findOneByWriter(Member writer) {
        QComment comment = QComment.comment;

        return queryFactory.selectFrom(comment)
                .join(comment.item).fetchJoin()
                .join(comment.writer).fetchJoin()
                .where(comment.writer.eq(writer))
                .fetchOne();
    }

    public Page<Comment> findCommentsByItem(Item item, Pageable pageable) {
        QComment comment = QComment.comment;

        List<Comment> content = queryFactory.selectFrom(comment)
                .join(comment.item)
                .join(comment.writer)
                .where(comment.item.eq(item))
                .orderBy(comment.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, content.size());
    }

    public void editContent(String content, @Param("id") Long id) {
        QComment comment = QComment.comment;

        queryFactory.update(comment)
                .set(comment.content, content)
                .where(comment.id.eq(id))
                .execute();
    }

    public void deleteBulkCommentsByItem(Item item) {
        QComment comment = QComment.comment;

        queryFactory.delete(comment)
                .where(comment.item.eq(item))
                .execute();
    }
}
