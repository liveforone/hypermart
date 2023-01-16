package hypermart.hypermart.comment.util;

import hypermart.hypermart.comment.model.Comment;
import hypermart.hypermart.utility.CommonUtils;

public class CommentUtils {

    public static boolean isDuplicateComment(Comment comment) {
        return !CommonUtils.isNull(comment);
    }
}
