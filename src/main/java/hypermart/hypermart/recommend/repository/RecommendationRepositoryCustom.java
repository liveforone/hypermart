package hypermart.hypermart.recommend.repository;

import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.member.model.Member;
import hypermart.hypermart.recommend.model.Recommendation;

public interface RecommendationRepositoryCustom {

    Recommendation findOneByMemberAndItem(Member member, Item item);

    void deleteBulkRecommendationsByItem(Item item);
}
