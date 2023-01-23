package hypermart.hypermart.recommend.repository;

import hypermart.hypermart.recommend.model.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long>, RecommendationRepositoryCustom {
}
