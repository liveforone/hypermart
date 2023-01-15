package hypermart.hypermart.recommend.repository;

import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.member.model.Member;
import hypermart.hypermart.recommend.model.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

    @Query("select r from Recommendation r join fetch r.member join fetch r.item where r.member = :member and r.item = :item")
    Recommendation findOneForDuplicate(@Param("member") Member member, @Param("item") Item item);
}
