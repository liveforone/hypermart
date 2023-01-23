package hypermart.hypermart.member.repository;

import hypermart.hypermart.member.model.MemberGrade;
import hypermart.hypermart.member.model.Role;

public interface MemberRepositoryCustom {

    void updateAddress(String address, String email);

    void updateAuthToSeller(Role auth, MemberGrade memberGrade, String email);

    void updateEmail(String oldEmail, String newEmail);

    void updatePassword(Long id, String password);

    void increaseSingleOrderCount(String email);

    void increaseMultipleOrderCount(int orderCount, String email);

    void decreaseOrderCount(String email);

    void updateGradeToSilver(MemberGrade memberGrade);

    void updateGradeToGold(MemberGrade memberGrade);

    void updateGradeToPlatinum(MemberGrade memberGrade);

    void updateGradeToDia(MemberGrade memberGrade);
}
