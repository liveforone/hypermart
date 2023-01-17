package hypermart.hypermart.orders.controller;

import hypermart.hypermart.basket.model.Basket;
import hypermart.hypermart.basket.service.BasketService;
import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.item.service.ItemService;
import hypermart.hypermart.item.util.ItemRemainingCheck;
import hypermart.hypermart.member.service.MemberService;
import hypermart.hypermart.orders.dto.OrdersRequest;
import hypermart.hypermart.orders.dto.OrdersResponse;
import hypermart.hypermart.orders.service.OrdersService;
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
        memberService.updateSingleOrderCount(email);
        itemService.minusSingleRemaining(itemId);
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
        memberService.updateMultipleOrderCount(orderCount, email);
        itemService.minusMultipleRemaining(baskets);
        log.info("장바구니 주문 성공");
        basketService.deleteBasketsByEmail(email);
        log.info("장바구니 성공적으로 비움");

        String url = "/member/my-page";
        return CommonUtils.makeResponseEntityForRedirect(url, request);
    }
}