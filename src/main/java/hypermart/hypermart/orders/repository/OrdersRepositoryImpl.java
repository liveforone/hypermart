package hypermart.hypermart.orders.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.member.model.Member;
import hypermart.hypermart.member.model.QMember;
import hypermart.hypermart.orders.model.OrderState;
import hypermart.hypermart.orders.model.Orders;
import hypermart.hypermart.orders.model.QOrders;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrdersRepositoryImpl implements OrdersRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public Orders findOneById(Long id) {
        QOrders orders = QOrders.orders;

        return queryFactory.selectFrom(orders)
                .join(orders.item).fetchJoin()
                .where(orders.id.eq(id))
                .fetchOne();
    }

    public Page<Orders> findOrdersByEmail(String email, Pageable pageable) {
        QOrders orders = QOrders.orders;
        QMember member = QMember.member;

        List<Orders> content =  queryFactory.selectFrom(orders)
                .join(orders.item)
                .join(orders.member, member)
                .where(member.email.eq(email))
                .orderBy(orders.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, content.size());
    }

    public List<Orders> findOrdersByMemberAndItem(OrderState orderState, Member member, Item item) {
        QOrders orders = QOrders.orders;

        return queryFactory.selectFrom(orders)
                .join(orders.member).fetchJoin()
                .join(orders.item).fetchJoin()
                .where(
                        orders.orderState.eq(orderState),
                        orders.member.eq(member),
                        orders.item.eq(item)
                )
                .fetch();
    }

    public void cancelOrder(OrderState orderState, Long id) {
        QOrders orders = QOrders.orders;

        queryFactory.update(orders)
                .set(orders.orderState, orderState)
                .where(orders.id.eq(id))
                .execute();
    }

    public void deleteBulkOrdersByItem(Item item) {
        QOrders orders = QOrders.orders;

        queryFactory.delete(orders)
                .where(orders.item.eq(item))
                .execute();
    }
}
