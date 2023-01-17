package hypermart.hypermart.recommend.controller;

import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.item.service.ItemService;
import hypermart.hypermart.member.model.Member;
import hypermart.hypermart.member.service.MemberService;
import hypermart.hypermart.orders.model.Orders;
import hypermart.hypermart.orders.service.OrdersService;
import hypermart.hypermart.recommend.model.Recommendation;
import hypermart.hypermart.recommend.service.RecommendationService;
import hypermart.hypermart.recommend.util.RecommendationUtils;
import hypermart.hypermart.utility.CommonUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final ItemService itemService;
    private final MemberService memberService;
    private final OrdersService ordersService;

    @PostMapping("/recommend/{itemId}")
    public ResponseEntity<?> recommendItem(
            @PathVariable("itemId") Long itemId,
            Principal principal,
            HttpServletRequest request
    ) {
        Item item = itemService.getItemDetail(itemId);

        if (CommonUtils.isNull(item)) {
            return ResponseEntity.ok("존재하지 않는 상품입니다.");
        }

        String email = principal.getName();
        Member member = memberService.getMemberEntity(email);
        List<Orders> orders = ordersService.getOrdersByMemberAndItem(member, item);
        if (CommonUtils.isNull(orders)) {
            return ResponseEntity.ok("추천은 주문한 상품만 가능합니다.");
        }

        Recommendation recommendation = recommendationService.getRecommendationDetail(item, member);
        if (RecommendationUtils.isDuplicateRecommendation(recommendation)) {
            return ResponseEntity.ok("이미 추천한 상품입니다.");
        }

        recommendationService.saveRecommendation(item, member);
        itemService.increaseOneGood(itemId);
        log.info("상품 추천 성공");

        String url = "/item/" + itemId;
        return CommonUtils.makeResponseEntityForRedirect(url, request);
    }

    @DeleteMapping("/recommend/cancel/{itemId}")
    public ResponseEntity<?> cancelRecommendation(
            @PathVariable("itemId") Long itemId,
            Principal principal
    ) {
        Item item = itemService.getItemDetail(itemId);

        if (CommonUtils.isNull(item)) {
            return ResponseEntity.ok("존재하지 않는 상품입니다.");
        }

        String email = principal.getName();
        Member member = memberService.getMemberEntity(email);
        Recommendation recommendation = recommendationService.getRecommendationDetail(item, member);
        if (!RecommendationUtils.isDuplicateRecommendation(recommendation)) {
            return ResponseEntity.ok("추천하지 않은 상품은 취소가 불가능합니다.");
        }

        recommendationService.deleteRecommendation(recommendation);
        log.info("추천 취소 성공");

        return ResponseEntity.ok("해당 상품의 추천이 취소되었습니다.");
    }
}
