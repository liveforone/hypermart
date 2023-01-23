package hypermart.hypermart.recommend.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.item.model.QItem;
import hypermart.hypermart.member.model.Member;
import hypermart.hypermart.member.model.QMember;
import hypermart.hypermart.recommend.model.QRecommendation;
import hypermart.hypermart.recommend.model.Recommendation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RecommendationRepositoryImpl implements RecommendationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public Recommendation findOneByMemberAndItem(Member member, Item item) {
        QRecommendation recommendation = QRecommendation.recommendation;
        QMember qMember = QMember.member;
        QItem qItem = QItem.item;

        return queryFactory.selectFrom(recommendation)
                .join(recommendation.member, qMember).fetchJoin()
                .join(recommendation.item, qItem).fetchJoin()
                .where(qMember.eq(member).and(qItem.eq(item)))
                .fetchOne();
    }

    public void deleteBulkRecommendationsByItem(Item item) {
        QRecommendation recommendation = QRecommendation.recommendation;

        queryFactory.delete(recommendation)
                .where(recommendation.item.eq(item))
                .execute();
    }
}
