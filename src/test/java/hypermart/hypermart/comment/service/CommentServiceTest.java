package hypermart.hypermart.comment.service;

import hypermart.hypermart.comment.dto.CommentRequest;
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
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private EntityManager em;


    @Transactional
    private void createMember(String email) {
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
    }

    @Transactional
    private Long createItem(String email) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setTitle("test_snack");
        itemRequest.setContent("test_content");
        itemRequest.setCategory("food");
        itemRequest.setPrice(10000);
        itemRequest.setGood(0);
        Long itemId = itemService.savePost(itemRequest, email);
        em.flush();
        em.clear();
        return itemId;
    }

    @Transactional
    private Long createComment(Long itemId, String content, String email) {
        Item item = itemService.getItemDetail(itemId);
        Member member = memberService.getMemberEntity(email);
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setContent(content);
        return commentService.saveComment(commentRequest, item, member);
    }

    @Test
    @Transactional
    void editComment() {
        //given
        String email = "yc2222@gmail.com";
        String content = "content";
        createMember(email);
        Long itemId = createItem(email);
        Long commentId = createComment(itemId, content, email);

        //when
        String updatedContent = "updatedContent";
        commentService.editComment(updatedContent, commentId);
        em.flush();
        em.clear();

        //then
        Assertions
                .assertThat(commentService.getCommentDetailById(commentId).getContent())
                .isEqualTo(updatedContent);
    }
}