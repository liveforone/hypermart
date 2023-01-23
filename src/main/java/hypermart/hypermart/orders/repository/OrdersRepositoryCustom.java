package hypermart.hypermart.orders.repository;

import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.member.model.Member;
import hypermart.hypermart.orders.model.OrderState;
import hypermart.hypermart.orders.model.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrdersRepositoryCustom {

    Orders findOneById(Long id);

    Page<Orders> findOrdersByEmail(String email, Pageable pageable);

    List<Orders> findOrdersByMemberAndItem(OrderState orderState, Member member, Item item);

    void cancelOrder(OrderState orderState, Long id);

    void deleteBulkOrdersByItem(Item item);
}
