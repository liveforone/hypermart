package hypermart.hypermart.orders.repository;

import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.member.model.Member;
import hypermart.hypermart.orders.model.OrderState;
import hypermart.hypermart.orders.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    @Query("select o from Orders o join fetch o.member join fetch o.item where o.orderState = :orderState o.member = :member and o.item = :item")
    List<Orders> findOrdersByMemberAndItem(@Param("orderState") OrderState orderState, @Param("member") Member member, @Param("item") Item item);
}
