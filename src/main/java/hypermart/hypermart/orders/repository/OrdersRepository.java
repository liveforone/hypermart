package hypermart.hypermart.orders.repository;

import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.member.model.Member;
import hypermart.hypermart.orders.model.OrderState;
import hypermart.hypermart.orders.model.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    @Query("select o from Orders o join fetch o.item join fetch o.member where o.id = :id")
    Orders findOneById(@Param("id") Long id);

    @Query("select o from Orders o join o.item join o.member m where m.email = :email")
    Page<Orders> findOrdersByEmail(@Param("email") String email, Pageable pageable);

    @Query("select o from Orders o join fetch o.member join fetch o.item where o.orderState = :orderState and o.member = :member and o.item = :item")
    List<Orders> findOrdersByMemberAndItem(@Param("orderState") OrderState orderState, @Param("member") Member member, @Param("item") Item item);

    @Modifying
    @Query("update Orders o set o.orderState = :orderState where o.id = :id")
    void cancelOrder(@Param("orderState") OrderState orderState, @Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query("delete from Orders o where o.item = :item")
    void deleteBulkOrdersByItem(@Param("item") Item item);
}
