package hypermart.hypermart.item.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.item.model.QItem;
import hypermart.hypermart.member.model.QMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public Page<Item> findAllItems(Pageable pageable) {
        QItem item = QItem.item;

        List<Item> content = queryFactory.selectFrom(item)
                .join(item.writer)
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, content.size());
    }

    public Page<Item> searchByTitle(String title, Pageable pageable) {
        QItem item = QItem.item;

        List<Item> content = queryFactory.selectFrom(item)
                .join(item.writer)
                .where(item.title.contains(title))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, content.size());
    }

    public Page<Item> searchByCategory(String category, Pageable pageable) {
        QItem item = QItem.item;

        List<Item> content = queryFactory.selectFrom(item)
                .join(item.writer)
                .where(item.category.contains(category))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, content.size());
    }

    public List<Item> findItemsByWriter(String email) {
        QItem item = QItem.item;
        QMember member = QMember.member;

        return queryFactory.selectFrom(item)
                .join(item.writer, member).fetchJoin()
                .where(member.email.eq(email))
                .fetch();
    }

    public Item findOneById(Long id) {
        QItem item = QItem.item;

        return queryFactory.selectFrom(item)
                .join(item.writer).fetchJoin()
                .where(item.id.eq(id))
                .fetchOne();
    }

    public void updateContent(String content, Long id) {
        QItem item = QItem.item;

        queryFactory.update(item)
                .set(item.content, content)
                .where(item.id.eq(id))
                .execute();
    }

    public void restockItem(int remaining, Long id) {
        QItem item = QItem.item;

        queryFactory.update(item)
                .set(item.remaining, remaining)
                .where(item.id.eq(id))
                .execute();
    }

    public void decreaseMultipleRemaining(int orderQuantity, Long id) {
        QItem item = QItem.item;

        queryFactory.update(item)
                .set(item.remaining, item.remaining.add(-orderQuantity))
                .where(item.id.eq(id))
                .execute();
    }

    public void decreaseSingleRemaining(Long id) {
        QItem item = QItem.item;

        queryFactory.update(item)
                .set(item.remaining, item.remaining.add(-1))
                .where(item.id.eq(id))
                .execute();
    }

    public void increaseSingleRemaining(Long id) {
        QItem item = QItem.item;

        queryFactory.update(item)
                .set(item.remaining, item.remaining.add(1))
                .where(item.id.eq(id))
                .execute();
    }

    public void increaseGood(Long id) {
        QItem item = QItem.item;

        queryFactory.update(item)
                .set(item.good, item.good.add(1))
                .where(item.id.eq(id))
                .execute();
    }
}
