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
    void updateAddress(@Param("address") String address, @Param("email") String email);

    @Modifying
    @Query("update Member m set m.auth = :auth, m.memberGrade = :memberGrade where m.email = :email")
    void updateAuthToSeller(@Param("auth") Role auth, @Param("memberGrade") MemberGrade memberGrade, @Param("email") String email);

    @Modifying
    @Query("update Member m set m.email = :newEmail where m.email = :oldEmail")
    void updateEmail(@Param("oldEmail") String oldEmail, @Param("newEmail") String newEmail);

    @Modifying
    @Query("update Member m set m.password = :password where m.id = :id")
    void updatePassword(@Param("id") Long id, @Param("password") String password);

    @Modifying
    @Query("update Member m set m.orderCount = m.orderCount + 1 where m.email = :email")
    void increaseSingleOrderCount(@Param("email") String email);

    @Modifying
    @Query("update Member m set m.orderCount = m.orderCount + :orderCount where m.email = :email")
    void increaseMultipleOrderCount(@Param("orderCount") int orderCount, @Param("email") String email);

    @Modifying
    @Query("update Member m set m.orderCount = m.orderCount - 1 where m.email = :email")
    void decreaseOrderCount(@Param("email") String email);

    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.memberGrade = :memberGrade where m.orderCount > 14")
    void updateGradeToSilver(@Param("memberGrade") MemberGrade memberGrade);

    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.memberGrade = :memberGrade where m.orderCount > 29")
    void updateGradeToGold(@Param("memberGrade") MemberGrade memberGrade);

    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.memberGrade = :memberGrade where m.orderCount > 59")
    void updateGradeToPlatinum(@Param("memberGrade") MemberGrade memberGrade);

    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.memberGrade = :memberGrade where m.orderCount > 119")
    void updateGradeToDia(@Param("memberGrade") MemberGrade memberGrade);
}
