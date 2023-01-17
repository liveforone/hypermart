package hypermart.hypermart.basket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasketResponse {

    private Long id;
    private Long itemId;
    private String title;
    private int price;
}
