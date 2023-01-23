package hypermart.hypermart.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hypermart.hypermart.member.model.MemberGrade;
import hypermart.hypermart.member.model.QMember;
import hypermart.hypermart.member.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public void updateAddress(String address, String email) {
        QMember member = QMember.member;

        queryFactory.update(member)
                .set(member.address, address)
                .where(member.email.eq(email))
                .execute();
    }

    public void updateAuthToSeller(Role auth, MemberGrade memberGrade, String email) {
        QMember member = QMember.member;

        queryFactory.update(member)
                .set(member.auth, auth)
                .set(member.memberGrade, memberGrade)
                .where(member.email.eq(email))
                .execute();
    }

    public void updateEmail(String oldEmail, String newEmail) {
        QMember member = QMember.member;

        queryFactory.update(member)
                .set(member.email, newEmail)
                .where(member.email.eq(oldEmail))
                .execute();
    }

    public void updatePassword(Long id, String password) {
        QMember member = QMember.member;

        queryFactory.update(member)
                .set(member.password, password)
                .where(member.id.eq(id))
                .execute();
    }

    public void increaseSingleOrderCount(String email) {
        QMember member = QMember.member;

        queryFactory.update(member)
                .set(member.orderCount, member.orderCount.add(1))
                .where(member.email.eq(email))
                .execute();
    }

    public void increaseMultipleOrderCount(int orderCount, String email) {
        QMember member = QMember.member;

        queryFactory.update(member)
                .set(member.orderCount, member.orderCount.add(orderCount))
                .where(member.email.eq(email))
                .execute();
    }

    public void decreaseOrderCount(String email) {
        QMember member = QMember.member;

        queryFactory.update(member)
                .set(member.orderCount, member.orderCount.add(-1))
                .where(member.email.eq(email))
                .execute();
    }

    public void updateGradeToSilver(MemberGrade memberGrade) {
        QMember member = QMember.member;

        queryFactory.update(member)
                .set(member.memberGrade, memberGrade)
                .where(member.orderCount.gt(14))
                .execute();
    }

    public void updateGradeToGold(MemberGrade memberGrade) {
        QMember member = QMember.member;

        queryFactory.update(member)
                .set(member.memberGrade, memberGrade)
                .where(member.orderCount.gt(29))
                .execute();
    }

    public void updateGradeToPlatinum(MemberGrade memberGrade) {
        QMember member = QMember.member;

        queryFactory.update(member)
                .set(member.memberGrade, memberGrade)
                .where(member.orderCount.gt(59))
                .execute();
    }

    public void updateGradeToDia(MemberGrade memberGrade) {
        QMember member = QMember.member;

        queryFactory.update(member)
                .set(member.memberGrade, memberGrade)
                .where(member.orderCount.gt(119))
                .execute();
    }
}
