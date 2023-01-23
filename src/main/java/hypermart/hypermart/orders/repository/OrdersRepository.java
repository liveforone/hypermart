package hypermart.hypermart.orders.repository;

import hypermart.hypermart.orders.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long>, OrdersRepositoryCustom {
}
