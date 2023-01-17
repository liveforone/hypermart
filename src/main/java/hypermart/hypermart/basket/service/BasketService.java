package hypermart.hypermart.basket.service;

import hypermart.hypermart.basket.dto.BasketResponse;
import hypermart.hypermart.basket.model.Basket;
import hypermart.hypermart.basket.repository.BasketRepository;
import hypermart.hypermart.basket.util.BasketMapper;
import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.member.model.Member;
import hypermart.hypermart.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasketService {

    private final BasketRepository basketRepository;
    private final MemberRepository memberRepository;

    public Basket getBasketDetail(Long id) {
        return basketRepository.findOneById(id);
    }

    public List<BasketResponse> getBasketsByEmail(String email) {
        return BasketMapper.entityToDtoList(
                basketRepository.findBasketsByEmail(email)
        );
    }

    @Transactional
    public void saveBasket(Item item, String email) {
        Member member = memberRepository.findByEmail(email);
        Basket basket = Basket.builder()
                .item(item)
                .member(member)
                .build();

        basketRepository.save(basket);
    }

    @Transactional
    public void deleteBasket(Long id) {
       basketRepository.deleteById(id);
    }

    @Transactional
    public void deleteBasketsByItem(Item item) {
        basketRepository.deleteBulkBasketsByItem(item);
    }
}
