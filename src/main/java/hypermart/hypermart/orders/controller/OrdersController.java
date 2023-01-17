package hypermart.hypermart.orders.controller;

import hypermart.hypermart.basket.model.Basket;
import hypermart.hypermart.basket.service.BasketService;
import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.item.service.ItemService;
import hypermart.hypermart.member.service.MemberService;
import hypermart.hypermart.orders.dto.OrdersRequest;
import hypermart.hypermart.orders.service.OrdersService;
import hypermart.hypermart.utility.CommonUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

        String email = principal.getName();
        ordersService.saveSingleOrder(item, email, ordersRequest);
        memberService.updateOrderCount(email);
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

        ordersService.saveBasketOrder(baskets);
        log.info("장바구니 주문 성공");
        basketService.deleteBasketsByEmail(email);
        log.info("장바구니 성공적으로 비움");

        String url = "/member/my-page";
        return CommonUtils.makeResponseEntityForRedirect(url, request);
    }
}
