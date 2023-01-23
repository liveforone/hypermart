package hypermart.hypermart.member.repository;

import hypermart.hypermart.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    Member findByEmail(String email);
}
