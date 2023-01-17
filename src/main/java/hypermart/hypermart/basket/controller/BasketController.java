package hypermart.hypermart.basket.controller;

import hypermart.hypermart.basket.dto.BasketResponse;
import hypermart.hypermart.basket.model.Basket;
import hypermart.hypermart.basket.service.BasketService;
import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.item.service.ItemService;
import hypermart.hypermart.utility.CommonUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BasketController {

    private final BasketService basketService;
    private final ItemService itemService;

    @GetMapping("/my-basket")
    public ResponseEntity<List<BasketResponse>> myBasket(Principal principal) {
        String email = principal.getName();
        List<BasketResponse> baskets = basketService.getBasketsByEmail(email);

        return ResponseEntity.ok(baskets);
    }

    @PostMapping("/basket/post/{itemId}")
    public ResponseEntity<?>postBasket(
            @PathVariable("itemId") Long itemId,
            Principal principal,
            HttpServletRequest request
    ) {
        Item item = itemService.getItemDetail(itemId);
        if (CommonUtils.isNull(item)) {
            return ResponseEntity.ok("존재하지 않는 상품입니다.");
        }

        String email = principal.getName();
        basketService.saveBasket(item, email);
        log.info("장바구니 저장성공");

        String url = "/item/" + itemId;
        return CommonUtils.makeResponseEntityForRedirect(url, request);
    }

    @DeleteMapping("/basket/delete/{basketId}")
    public ResponseEntity<?> deleteBasket(
            @PathVariable("basketId") Long basketId,
            Principal principal
    ) {
        Basket basket = basketService.getBasketDetail(basketId);
        if (CommonUtils.isNull(basket)) {
            return ResponseEntity.ok("존재하지 않는 장바구니 입니다.");
        }

        String email = principal.getName();
        String member = basket.getMember().getEmail();
        if (!Objects.equals(email, member)) {
            return ResponseEntity.ok("장바구니 주인만 삭제 가능합니다.");
        }

        basketService.deleteBasket(basketId);
        log.info("장바구니 삭제 성공");

        return ResponseEntity.ok("장바구니를 성공적으로 삭제하였습니다");
    }
}
