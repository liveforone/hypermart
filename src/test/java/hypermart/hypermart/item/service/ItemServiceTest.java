package hypermart.hypermart.item.service;

import hypermart.hypermart.item.dto.ItemRequest;
import hypermart.hypermart.member.dto.MemberRequest;
import hypermart.hypermart.member.service.MemberService;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class ItemServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private EntityManager em;

    @Transactional
    private Long createItem(String email) {
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
    void updateContentTest() {
        //given
        String email = "yc1111@gmail.com";
        Long itemId = createItem(email);

        //when
        String updatedContent = "updated Content";
        itemService.updateContent(updatedContent, itemId);
        em.flush();
        em.clear();

        //then
        Assertions
                .assertThat(itemService.getItemDetail(itemId).getContent())
                .isEqualTo(updatedContent);
    }

    @Test
    @Transactional
    void updateRemainingTest() {
        //given
        String email = "yc1111@gmail.com";
        Long itemId = createItem(email);

        //when
        int remaining = 10;
        itemService.updateRemaining(remaining, itemId);
        em.flush();
        em.clear();

        //then
        Assertions
                .assertThat(itemService.getItemDetail(itemId).getRemaining())
                .isEqualTo(remaining);
    }

    @Test
    @Transactional
    void increaseOneGoodTest() {
        //given
        String email = "yc1111@gmail.com";
        Long itemId = createItem(email);

        //when
        itemService.increaseOneGood(itemId);
        em.flush();
        em.clear();

        //then
        Assertions
                .assertThat(itemService.getItemDetail(itemId).getGood())
                .isEqualTo(1);
    }
}