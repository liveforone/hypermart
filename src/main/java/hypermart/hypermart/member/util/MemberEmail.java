package hypermart.hypermart.member.util;


import hypermart.hypermart.member.model.Member;
import hypermart.hypermart.utility.CommonUtils;

public class MemberEmail {
    public static boolean isDuplicateEmail(Member member) {
        return !CommonUtils.isNull(member);
    }
}
