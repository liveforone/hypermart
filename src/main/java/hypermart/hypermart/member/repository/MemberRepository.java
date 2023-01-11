package hypermart.hypermart.member.repository;

import hypermart.hypermart.member.model.Member;
import hypermart.hypermart.member.model.MemberGrade;
import hypermart.hypermart.member.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email);

    @Modifying
    @Query("update Member m set m.address = :address where m.email = :email")
    void regiAddress(@Param("address") String address, @Param("email") String email);

    @Modifying
    @Query("update Member m set m.auth = :auth, m.memberGrade = :memberGrade where m.email = :email")
    void regiSeller(@Param("auth") Role auth, @Param("memberGrade") MemberGrade memberGrade, @Param("email") String email);

    @Modifying
    @Query("update Member m set m.email = :newEmail where m.email = :oldEmail")
    void updateEmail(@Param("oldEmail") String oldEmail, @Param("newEmail") String newEmail);

    @Modifying
    @Query("update Member m set m.password = :password where m.id = :id")
    void updatePassword(@Param("id") Long id, @Param("password") String password);

    @Modifying
    @Query("update Member m set m.orderCount = m.orderCount + 1 where m.email = :email")
    void updateOrderCount(@Param("email") String email);

    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.memberGrade = :memberGrade where m.orderCount >= 15")
    void updateGradeToSilver(@Param("memberGrade") MemberGrade memberGrade);

    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.memberGrade = :memberGrade where m.orderCount >= 30")
    void updateGradeToGold(@Param("memberGrade") MemberGrade memberGrade);

    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.memberGrade = :memberGrade where m.orderCount >= 60")
    void updateGradeToPlatinum(@Param("memberGrade") MemberGrade memberGrade);

    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.memberGrade = :memberGrade where m.orderCount >= 120")
    void updateGradeToDia(@Param("memberGrade") MemberGrade memberGrade);
}
