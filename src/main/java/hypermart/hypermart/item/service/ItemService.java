package hypermart.hypermart.item.service;

import hypermart.hypermart.item.dto.ItemResponse;
import hypermart.hypermart.item.repository.ItemRepository;
import hypermart.hypermart.item.util.ItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;

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
}
