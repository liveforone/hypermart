package hypermart.hypermart.basket.repository;

import hypermart.hypermart.basket.model.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketRepository extends JpaRepository<Basket, Long>, BasketRepositoryCustom {
}
