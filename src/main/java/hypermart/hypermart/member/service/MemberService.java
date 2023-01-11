package hypermart.hypermart.member.service;

import hypermart.hypermart.jwt.JwtTokenProvider;
import hypermart.hypermart.jwt.TokenInfo;
import hypermart.hypermart.member.dto.MemberRequest;
import hypermart.hypermart.member.model.Member;
import hypermart.hypermart.member.model.MemberGrade;
import hypermart.hypermart.member.model.Role;
import hypermart.hypermart.member.repository.MemberRepository;
import hypermart.hypermart.member.util.MemberMapper;
import hypermart.hypermart.member.util.MemberPassword;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    public Member getMemberEntity(String email) {
        return memberRepository.findByEmail(email);
    }

    /*
     * 모든 유저 반환
     * when : 권한이 어드민인 유저가 호출할때
     */
    public List<Member> getAllMemberForAdmin() {
        return memberRepository.findAll();
    }

    @Transactional
    public void signup(MemberRequest memberRequest) {
        memberRequest.setPassword(
                MemberPassword.encodePassword(memberRequest.getPassword())
        );

        if (Objects.equals(memberRequest.getEmail(), "admin@coffeebank.com")) {
            memberRequest.setAuth(Role.ADMIN);
            memberRequest.setMemberGrade(MemberGrade.ADMIN);
        }else {
            memberRequest.setAuth(Role.MEMBER);
            memberRequest.setMemberGrade(MemberGrade.BRONZE);
        }

        memberRepository.save(
                MemberMapper.dtoToEntity(memberRequest)
        );
    }

    @Transactional
    public TokenInfo login(MemberRequest memberRequest) {
        String email = memberRequest.getEmail();
        String password = memberRequest.getPassword();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                email,
                password
        );
        Authentication authentication = authenticationManagerBuilder
                .getObject()
                .authenticate(authenticationToken);

        return jwtTokenProvider
                .generateToken(authentication);
    }

    @Transactional
    public void regiAddress(String address, String email) {
        memberRepository.regiAddress(address, email);
    }

    @Transactional
    public void regiSeller(String email) {
        memberRepository.regiSeller(Role.SELLER, MemberGrade.SELLER, email);
    }

    @Transactional
    public void updateEmail(String oldEmail, String newEmail) {
        memberRepository.updateEmail(oldEmail, newEmail);
    }

    @Transactional
    public void updatePassword(Long id, String inputPassword) {
        String newPassword = MemberPassword.encodePassword(inputPassword);

        memberRepository.updatePassword(id, newPassword);
    }

    @Transactional
    public void deleteUser(Long id) {
        memberRepository.deleteById(id);
    }

    @Scheduled(cron = "0 0 23 * * *")
    @Transactional
    public void updateAllMemberGrade() {
        memberRepository.updateGradeToSilver(MemberGrade.SILVER);
        memberRepository.updateGradeToGold(MemberGrade.GOLD);
        memberRepository.updateGradeToPlatinum(MemberGrade.PLATINUM);
        memberRepository.updateGradeToDia(MemberGrade.DIA);
    }
}
