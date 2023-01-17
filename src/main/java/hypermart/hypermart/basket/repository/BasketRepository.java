package hypermart.hypermart.basket.repository;

import hypermart.hypermart.basket.model.Basket;
import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BasketRepository extends JpaRepository<Basket, Long> {

    @Query("select b from Basket b join fetch b.item join fetch b.member where b.id = :id")
    Basket findOneById(@Param("id") Long id);

    @Query("select b from Basket b join fetch b.item join fetch b.member m where m.email = :email")
    List<Basket> findBasketsByEmail(@Param("email") String email);

    @Modifying(clearAutomatically = true)
    @Query("delete from Basket b where b.member = :member")
    void deleteBulkBasketsByMember(@Param("member") Member member);

    @Modifying(clearAutomatically = true)
    @Query("delete from Basket b where b.item = :item")
    void deleteBulkBasketsByItem(@Param("item") Item item);
}
