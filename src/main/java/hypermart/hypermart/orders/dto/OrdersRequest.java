package hypermart.hypermart.orders.dto;

import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.member.model.Member;
import hypermart.hypermart.orders.model.OrderState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdersRequest {

    private Long id;
    private OrderState orderState;
    private int orderQuantity;
    private int totalPrice;
    private Member member;
    private Item item;
}
