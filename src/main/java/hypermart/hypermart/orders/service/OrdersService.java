package hypermart.hypermart.orders.service;

import hypermart.hypermart.basket.model.Basket;
import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.member.model.Member;
import hypermart.hypermart.member.repository.MemberRepository;
import hypermart.hypermart.orders.dto.OrdersRequest;
import hypermart.hypermart.orders.model.OrderState;
import hypermart.hypermart.orders.model.Orders;
import hypermart.hypermart.orders.repository.OrdersRepository;
import hypermart.hypermart.orders.util.DiscountPolicy;
import hypermart.hypermart.orders.util.OrderClock;
import hypermart.hypermart.orders.util.OrdersMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final MemberRepository memberRepository;

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
        int orderCount = ordersRequest.getOrderCount();

        if (OrderClock.isSpecialDiscountTime()) {
            ordersRequest.setTotalPrice(
                    DiscountPolicy.calculateSpecialDiscount(item, orderCount)
            );
        } else {
            ordersRequest.setTotalPrice(
                    DiscountPolicy.calculateDiscount(item, member, orderCount)
            );
        }
        ordersRequest.setOrderState(OrderState.ORDER);
        ordersRequest.setItem(item);
        ordersRequest.setMember(member);
        ordersRepository.save(OrdersMapper.dtoToEntity(ordersRequest));
    }

    @Transactional
    public void saveBasketOrder(List<Basket> baskets) {
        int orderCount = 1;

        for (Basket basket : baskets) {
            Item item = basket.getItem();
            Member member = basket.getMember();

            OrdersRequest ordersRequest = new OrdersRequest();
            if (OrderClock.isSpecialDiscountTime()) {
                ordersRequest.setTotalPrice(
                        DiscountPolicy.calculateSpecialDiscount(item, orderCount)
                );
            } else {
                ordersRequest.setTotalPrice(
                        DiscountPolicy.calculateDiscount(item, member, orderCount)
                );
            }
            ordersRequest.setOrderCount(orderCount);
            ordersRequest.setOrderState(OrderState.ORDER);
            ordersRequest.setItem(item);
            ordersRequest.setMember(member);
            ordersRepository.save(OrdersMapper.dtoToEntity(ordersRequest));
        }
    }
}
