package hypermart.hypermart.basket.repository;

import hypermart.hypermart.basket.model.Basket;
import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.member.model.Member;

import java.util.List;

public interface BasketRepositoryCustom {

    Basket findOneById(Long id);

    List<Basket> findBasketsByEmail(String email);

    void deleteBulkBasketsByMember(Member member);

    void deleteBulkBasketsByItem(Item item);
}
