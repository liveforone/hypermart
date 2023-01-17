package hypermart.hypermart.orders.service;

import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.member.model.Member;
import hypermart.hypermart.orders.model.Orders;
import hypermart.hypermart.orders.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrdersService {

    private final OrdersRepository ordersRepository;

    public List<Orders> getOrdersByMemberAndItem(Member member, Item item) {
        return ordersRepository.findOrdersByMemberAndItem(member, item);
    }
}
