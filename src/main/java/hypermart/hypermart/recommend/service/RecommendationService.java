package hypermart.hypermart.recommend.service;

import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.member.model.Member;
import hypermart.hypermart.recommend.model.Recommendation;
import hypermart.hypermart.recommend.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;

    public Recommendation getRecommendationDetail(Item item, Member member) {
        return recommendationRepository.findOneByMemberAndItem(member, item);
    }

    @Transactional
    public Long saveRecommendation(Item item, Member member) {
        Recommendation recommendation = new Recommendation();
        recommendation.setItem(item);
        recommendation.setMember(member);
        return recommendationRepository.save(recommendation).getId();
    }

    @Transactional
    public void deleteRecommendation(Recommendation recommendation) {
        recommendationRepository.deleteById(recommendation.getId());
    }

    @Transactional
    public void deleteRecommendationsByItem(Item item) {
        recommendationRepository.deleteBulkRecommendationsByItem(item);
    }
}
