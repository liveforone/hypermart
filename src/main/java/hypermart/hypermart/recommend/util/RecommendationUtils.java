package hypermart.hypermart.recommend.util;

import hypermart.hypermart.recommend.model.Recommendation;
import hypermart.hypermart.utility.CommonUtils;

public class RecommendationUtils {

    public static boolean isDuplicateRecommendation(Recommendation recommendation) {
        return !CommonUtils.isNull(
                recommendation
        );
    }
}
