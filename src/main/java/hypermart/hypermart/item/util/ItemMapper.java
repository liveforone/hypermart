package hypermart.hypermart.item.util;

import hypermart.hypermart.item.dto.ItemRequest;
import hypermart.hypermart.item.dto.ItemResponse;
import hypermart.hypermart.item.model.Item;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {

    public static Item dtoToEntity(ItemRequest itemRequest) {
        return Item.builder()
                .id(itemRequest.getId())
                .title(itemRequest.getTitle())
                .content(itemRequest.getContent())
                .category(itemRequest.getCategory())
                .price(itemRequest.getPrice())
                .remaining(itemRequest.getRemaining())
                .good(itemRequest.getGood())
                .writer(itemRequest.getWriter())
                .build();
    }

    private static ItemResponse dtoBuilder(Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .title(item.getTitle())
                .content(item.getContent())
                .category(item.getCategory())
                .writer(item.getWriter().getEmail())
                .price(item.getPrice())
                .remaining(item.getRemaining())
                .good(item.getGood())
                .build();
    }

    public static Page<ItemResponse> entityToDtoPage(Page<Item> items) {
        return items.map(ItemMapper::dtoBuilder);
    }

    public static List<ItemResponse> entityToDtoList(List<Item> items) {
        return items
                .stream()
                .map(ItemMapper::dtoBuilder)
                .collect(Collectors.toList());
    }

    public static ItemResponse entityToDtoDetail(Item item) {
        return ItemMapper.dtoBuilder(item);
    }
}
