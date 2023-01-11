package hypermart.hypermart.member.util;


import hypermart.hypermart.member.dto.MemberRequest;
import hypermart.hypermart.member.dto.MemberResponse;
import hypermart.hypermart.member.model.Member;

public class MemberMapper {

    public static Member dtoToEntity(MemberRequest memberRequest) {
        return Member.builder()
                .id(memberRequest.getId())
                .email(memberRequest.getEmail())
                .password(memberRequest.getPassword())
                .auth(memberRequest.getAuth())
                .memberGrade(memberRequest.getMemberGrade())
                .orderCount(memberRequest.getOrderCount())
                .address(memberRequest.getAddress())
                .build();
    }

    public static MemberResponse dtoBuilder(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .auth(member.getAuth())
                .memberGrade(member.getMemberGrade())
                .orderCount(member.getOrderCount())
                .address(member.getAddress())
                .build();
    }
}
