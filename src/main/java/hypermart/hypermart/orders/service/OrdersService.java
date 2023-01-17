package hypermart.hypermart.orders.service;

import hypermart.hypermart.basket.model.Basket;
import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.member.model.Member;
import hypermart.hypermart.member.repository.MemberRepository;
import hypermart.hypermart.orders.dto.OrdersRequest;
import hypermart.hypermart.orders.dto.OrdersResponse;
import hypermart.hypermart.orders.model.OrderState;
import hypermart.hypermart.orders.model.Orders;
import hypermart.hypermart.orders.repository.OrdersRepository;
import hypermart.hypermart.orders.util.DiscountPolicy;
import hypermart.hypermart.orders.util.OrderClock;
import hypermart.hypermart.orders.util.OrdersMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final MemberRepository memberRepository;

    public Page<OrdersResponse> getOrdersByEmail(String email, Pageable pageable) {
        return OrdersMapper.entityToDtoPage(
                ordersRepository.findOrdersByEmail(email, pageable)
        );
    }

    public List<Orders> getOrdersByMemberAndItem(Member member, Item item) {
        return ordersRepository.findOrdersByMemberAndItem(
                OrderState.ORDER,
                member,
                item
        );
    }

    @Transactional
    public void saveSingleOrder(Item item, String email, OrdersRequest ordersRequest) {
        Member member = memberRepository.findByEmail(email);
        int orderQuantity = ordersRequest.getOrderQuantity();
        int totalPrice;

        if (OrderClock.isSpecialDiscountTime()) {
            totalPrice = DiscountPolicy.calculateSpecialDiscount(item, orderQuantity);
        } else {
            totalPrice = DiscountPolicy.calculateDiscount(item, member, orderQuantity);
        }

        ordersRequest = OrdersRequest.builder()
                .totalPrice(totalPrice)
                .orderQuantity(orderQuantity)
                .orderState(OrderState.ORDER)
                .item(item)
                .member(member)
                .build();
        ordersRepository.save(OrdersMapper.dtoToEntity(ordersRequest));
    }

    @Transactional
    public void saveBasketOrder(List<Basket> baskets) {
        int orderQuantity = 1;

        for (Basket basket : baskets) {
            Item item = basket.getItem();
            Member member = basket.getMember();
            int totalPrice;

            if (OrderClock.isSpecialDiscountTime()) {
                totalPrice = DiscountPolicy.calculateSpecialDiscount(item, orderQuantity);
            } else {
                totalPrice = DiscountPolicy.calculateDiscount(item, member, orderQuantity);
            }

            OrdersRequest ordersRequest = OrdersRequest.builder()
                    .orderQuantity(orderQuantity)
                    .orderState(OrderState.ORDER)
                    .totalPrice(totalPrice)
                    .item(item)
                    .member(member)
                    .build();
            ordersRepository.save(OrdersMapper.dtoToEntity(ordersRequest));
        }
    }
}
