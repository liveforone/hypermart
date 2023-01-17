package hypermart.hypermart.orders.dto;

import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.member.model.Member;
import hypermart.hypermart.orders.model.OrderState;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrdersRequest {

    private Long id;
    private OrderState orderState;
    private int orderCount;
    private int totalPrice;
    private Member member;
    private Item item;
}
