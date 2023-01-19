package hypermart.hypermart.orders.controller;

import hypermart.hypermart.basket.model.Basket;
import hypermart.hypermart.basket.service.BasketService;
import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.item.service.ItemService;
import hypermart.hypermart.item.util.ItemRemainingCheck;
import hypermart.hypermart.member.service.MemberService;
import hypermart.hypermart.orders.dto.OrdersRequest;
import hypermart.hypermart.orders.dto.OrdersResponse;
import hypermart.hypermart.orders.model.Orders;
import hypermart.hypermart.orders.service.OrdersService;
import hypermart.hypermart.orders.util.OrderCancel;
import hypermart.hypermart.utility.CommonUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrdersController {

    private final OrdersService ordersService;
    private final ItemService itemService;
    private final MemberService memberService;
    private final BasketService basketService;

    @GetMapping("/my-order")
    public ResponseEntity<Page<OrdersResponse>> myOrderPage(
            @PageableDefault(page = 0, size = 10)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "id", direction = Sort.Direction.DESC)
            }) Pageable pageable,
            Principal principal
    ) {
        String email = principal.getName();
        Page<OrdersResponse> orders = ordersService.getOrdersByEmail(email, pageable);

        return ResponseEntity.ok(orders);
    }

    @PostMapping("/order/single/{itemId}")
    public ResponseEntity<?> postSingleOrder(
            @PathVariable("itemId") Long itemId,
            @RequestBody OrdersRequest ordersRequest,
            Principal principal,
            HttpServletRequest request
    ) {
        Item item = itemService.getItemDetail(itemId);
        if (CommonUtils.isNull(item)) {
            return ResponseEntity.ok("존재하지 않는 상품입니다.");
        }

        int remaining = item.getRemaining();
        if (ItemRemainingCheck.isSoldOut(remaining)) {
            return ResponseEntity.ok("품절된 상품입니다.");
        }

        int orderQuantity = ordersRequest.getOrderQuantity();
        if (ItemRemainingCheck.isOverRemaining(remaining, orderQuantity)) {
            return ResponseEntity.ok("주문 수량이 재고를 초과합니다.");
        }

        String email = principal.getName();
        ordersService.saveSingleOrder(item, email, ordersRequest);
        memberService.increaseSingleOrderCount(email);
        itemService.decreaseMultipleRemainingForSingleOrder(itemId, orderQuantity);
        log.info("단일 주문 성공");

        String url = "/item/" + itemId;
        return CommonUtils.makeResponseEntityForRedirect(url, request);
    }

    @PostMapping("/order/basket")
    public ResponseEntity<?> postBasketOrder(
            Principal principal,
            HttpServletRequest request
    ) {
        String email = principal.getName();
        List<Basket> baskets = basketService.getBasketsByEmail(email);

        if (CommonUtils.isNull(baskets)) {
            return ResponseEntity.ok("장바구니가 비어있습니다.");
        }

        if (ItemRemainingCheck.isSoldOutForMultiple(baskets)) {
            return ResponseEntity.ok("품절된 상품입니다.");
        }

        int orderCount = baskets.size();
        ordersService.saveBasketOrder(baskets);
        memberService.increaseMultipleOrderCount(orderCount, email);
        itemService.decreaseSingleRemainingForBasketOrder(baskets);
        log.info("장바구니 주문 성공");
        basketService.deleteBasketsByEmail(email);
        log.info("장바구니 성공적으로 비움");

        String url = "/member/my-page";
        return CommonUtils.makeResponseEntityForRedirect(url, request);
    }

    @PutMapping("/order/cancel/{id}")
    public ResponseEntity<?> cancelOrder(
            @PathVariable("id") Long id,
            Principal principal
    ) {
        Orders orders = ordersService.getOrderDetail(id);
        if (CommonUtils.isNull(orders)) {
            return ResponseEntity.ok("존재하지 않는 주문입니다.");
        }

        String email = principal.getName();
        String member = orders.getMember().getEmail();
        if (!Objects.equals(email, member)) {
            return ResponseEntity.ok("주문한 사람만 주문취소가 가능합니다.");
        }

        if (OrderCancel.isOverCancelLimitDate(orders)) {
            log.info("주문 취소 실패");
            return ResponseEntity.ok("주문취소 기간이 지났습니다.\n주문 취소가 불가능합니다.");
        }

        ordersService.cancelOrder(id);
        log.info("주문취소");
        memberService.decreaseOrderCount(email);
        log.info("주문 횟수 복구 성공");
        Long itemId = orders.getItem().getId();
        itemService.increaseSingleRemaining(itemId);
        log.info("상품 재고 복구 성공");

        return ResponseEntity.ok("주문을 성공적으로 취소하였습니다");
    }
}
