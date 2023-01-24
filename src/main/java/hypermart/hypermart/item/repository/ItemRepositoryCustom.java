package hypermart.hypermart.item.repository;

import hypermart.hypermart.item.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemRepositoryCustom {

    Page<Item> findAllItems(Pageable pageable);

    Page<Item> searchByTitle(String title, Pageable pageable);

    Page<Item> searchByCategory(String category, Pageable pageable);

    List<Item> findItemsByWriter(String email);

    Item findOneById(Long id);

    void updateContent(String content, Long id);

    void restockItem(int remaining, Long id);

    void decreaseMultipleRemaining(int orderQuantity, Long id);

    void decreaseSingleRemaining(Long id);

    void increaseSingleRemaining(Long id);

    void increaseGood(Long id);
}
