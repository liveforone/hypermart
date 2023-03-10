package hypermart.hypermart.item.service;

import hypermart.hypermart.basket.model.Basket;
import hypermart.hypermart.item.dto.ItemRequest;
import hypermart.hypermart.item.dto.ItemResponse;
import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.item.repository.ItemRepository;
import hypermart.hypermart.item.util.ItemMapper;
import hypermart.hypermart.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    public Page<ItemResponse> getItems(Pageable pageable) {
        return ItemMapper.entityToDtoPage(
                itemRepository.findAllItems(pageable)
        );
    }

    public Page<ItemResponse> searchItemsByTitle(String title, Pageable pageable) {
        return ItemMapper.entityToDtoPage(
                itemRepository.searchByTitle(title, pageable)
        );
    }

    public Page<ItemResponse> searchItemsByCategory(String category, Pageable pageable) {
        return ItemMapper.entityToDtoPage(
                itemRepository.searchByCategory(category, pageable)
        );
    }

    public List<ItemResponse> getItemsByWriter(String email) {
        return ItemMapper.entityToDtoList(
                itemRepository.findItemsByWriter(email)
        );
    }

    public Item getItemDetail(Long id) {
        return itemRepository.findOneById(id);
    }

    @Transactional
    public Long savePost(ItemRequest itemRequest, String email) {
        itemRequest.setWriter(
                memberRepository.findByEmail(email)
        );
        return itemRepository.save(
                ItemMapper.dtoToEntity(itemRequest)
        ).getId();
    }

    @Transactional
    public void updateContent(String content, Long id) {
        itemRepository.updateContent(content, id);
    }

    @Transactional
    public void restockItem(int inputRemaining, Long id) {
        itemRepository.restockItem(inputRemaining, id);
    }

    @Transactional
    public void decreaseMultipleRemainingForSingleOrder(Long id, int orderQuantity) {
        itemRepository.decreaseMultipleRemaining(orderQuantity, id);
    }

    @Transactional
    public void decreaseSingleRemainingForBasketOrder(List<Basket> baskets) {
        for (Basket basket : baskets) {
            Long itemId = basket.getItem().getId();
            itemRepository.decreaseSingleRemaining(itemId);
        }
    }

    @Transactional
    public void increaseSingleRemaining(Long id) {
        itemRepository.increaseSingleRemaining(id);
    }

    @Transactional
    public void increaseOneGood(Long id) {
        itemRepository.increaseGood(id);
    }

    @Transactional
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }
}
