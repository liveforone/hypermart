package hypermart.hypermart.basket.util;

import hypermart.hypermart.basket.dto.BasketResponse;
import hypermart.hypermart.basket.model.Basket;

import java.util.List;
import java.util.stream.Collectors;

public class BasketMapper {

    private static BasketResponse dtoBuilder(Basket basket) {
        return BasketResponse.builder()
                .id(basket.getId())
                .itemId(basket.getItem().getId())
                .title(basket.getItem().getTitle())
                .price(basket.getItem().getPrice())
                .build();
    }

    public static List<BasketResponse> entityToDtoList(List<Basket> baskets) {
        return baskets
                .stream()
                .map(BasketMapper::dtoBuilder)
                .collect(Collectors.toList());
    }
}
