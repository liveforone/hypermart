package hypermart.hypermart.orders.util;

import hypermart.hypermart.orders.dto.OrdersRequest;
import hypermart.hypermart.orders.dto.OrdersResponse;
import hypermart.hypermart.orders.model.Orders;
import org.springframework.data.domain.Page;

public class OrdersMapper {

    public static Orders dtoToEntity(OrdersRequest ordersRequest) {
        return Orders.builder()
                .id(ordersRequest.getId())
                .orderState(ordersRequest.getOrderState())
                .orderCount(ordersRequest.getOrderCount())
                .totalPrice(ordersRequest.getTotalPrice())
                .member(ordersRequest.getMember())
                .item(ordersRequest.getItem())
                .build();
    }

    private static OrdersResponse dtoBuilder(Orders orders) {
        return OrdersResponse.builder()
                .id(orders.getId())
                .orderState(orders.getOrderState())
                .orderCount(orders.getOrderCount())
                .totalPrice(orders.getTotalPrice())
                .itemId(orders.getItem().getId())
                .itemTitle(orders.getItem().getTitle())
                .createdDate(orders.getCreatedDate())
                .build();
    }

    public static Page<OrdersResponse> entityToDtoPage(Page<Orders> orders) {
        return orders.map(OrdersMapper::dtoBuilder);
    }
}
