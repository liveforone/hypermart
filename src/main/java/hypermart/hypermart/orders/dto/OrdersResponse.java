package hypermart.hypermart.orders.dto;

import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.member.model.Member;
import hypermart.hypermart.orders.model.OrderState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdersResponse {

    private Long id;
    private OrderState orderState;
    private int orderCount;
    private int totalPrice;
    private Long itemId;
    private String itemTitle;
    private LocalDate createdDate;
}
