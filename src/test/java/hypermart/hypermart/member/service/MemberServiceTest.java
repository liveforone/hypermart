package hypermart.hypermart.member.service;

import hypermart.hypermart.member.dto.MemberRequest;
import hypermart.hypermart.member.model.MemberGrade;
import hypermart.hypermart.member.model.Role;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private EntityManager em;

    @Transactional
    private void createMember(String email, int orderCount) {
        String password = "1111";
        String address = "seoul";
        MemberRequest memberRequest = new MemberRequest();
        memberRequest.setEmail(email);
        memberRequest.setPassword(password);
        memberRequest.setOrderCount(orderCount);
        memberRequest.setAddress(address);
        memberService.signup(memberRequest);
    }

    @Test
    @Transactional
    void regiSellerTest() {
        //given
        String email = "yc1111@gmail.com";
        int orderCount = 15;
        createMember(email, orderCount);

        //when
        memberService.regiSeller(email);
        em.flush();
        em.clear();

        //then
        Assertions
                .assertThat(memberService.getMemberEntity(email).getAuth())
                .isEqualTo(Role.SELLER);
    }

    @Test
    @Transactional
    void updateAllMemberGradeTest() {
        //given
        String email = "yc1111@gmail.com";
        int orderCount = 15;
        createMember(email, orderCount);

        //when
        memberService.updateAllMemberGrade();
        em.flush();
        em.clear();

        //then
        Assertions.assertThat(memberService.getMemberEntity(email).getMemberGrade())
                .isEqualTo(MemberGrade.SILVER);
    }
}