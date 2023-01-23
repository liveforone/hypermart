package hypermart.hypermart.basket.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hypermart.hypermart.basket.model.Basket;
import hypermart.hypermart.basket.model.QBasket;
import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.member.model.Member;
import hypermart.hypermart.member.model.QMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BasketRepositoryImpl implements BasketRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public Basket findOneById(Long id) {
        QBasket basket = QBasket.basket;

        return queryFactory.selectFrom(basket)
                .join(basket.item).fetchJoin()
                .join(basket.member).fetchJoin()
                .where(basket.id.eq(id))
                .fetchOne();
    }

    public List<Basket> findBasketsByEmail(String email) {
        QBasket basket = QBasket.basket;
        QMember member = QMember.member;

        return queryFactory.selectFrom(basket)
                .join(basket.item).fetchJoin()
                .join(basket.member, member).fetchJoin()
                .where(member.email.eq(email))
                .fetch();
    }

    public void deleteBulkBasketsByMember(Member member) {
        QBasket basket = QBasket.basket;

        queryFactory.delete(basket)
                .where(basket.member.eq(member))
                .execute();
    }

    public void deleteBulkBasketsByItem(Item item) {
        QBasket basket = QBasket.basket;

        queryFactory.delete(basket)
                .where(basket.item.eq(item))
                .execute();
    }
}
