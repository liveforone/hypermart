package hypermart.hypermart.recommend.service;

import hypermart.hypermart.item.dto.ItemRequest;
import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.item.service.ItemService;
import hypermart.hypermart.member.dto.MemberRequest;
import hypermart.hypermart.member.model.Member;
import hypermart.hypermart.member.service.MemberService;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class RecommendationServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private EntityManager em;

    @Transactional
    private Long createMemberAndItem(String email) {
        String password = "1111";
        int orderCount = 10;
        String address = "seoul";
        MemberRequest memberRequest = new MemberRequest();
        memberRequest.setEmail(email);
        memberRequest.setPassword(password);
        memberRequest.setOrderCount(orderCount);
        memberRequest.setAddress(address);
        memberService.signup(memberRequest);
        em.flush();
        em.clear();

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setTitle("test_snack");
        itemRequest.setContent("test_content");
        itemRequest.setCategory("food");
        itemRequest.setPrice(10000);
        itemRequest.setGood(0);
        return itemService.savePost(itemRequest, email);
    }

    @Test
    @Transactional
    void saveRecommendationTest() {
        //given
        String email = "yc1111@naver.com";
        Long itemId = createMemberAndItem(email);
        em.flush();
        em.clear();

        //when
        Item item = itemService.getItemDetail(itemId);
        Member member = memberService.getMemberEntity(email);
        Long recommendationId = recommendationService.saveRecommendation(item, member);
        em.flush();
        em.clear();

        //then
        Assertions
                .assertThat(recommendationService.getRecommendationDetail(item, member).getId())
                .isEqualTo(recommendationId);
    }
}