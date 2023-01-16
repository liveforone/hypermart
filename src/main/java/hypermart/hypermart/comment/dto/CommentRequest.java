package hypermart.hypermart.comment.dto;

import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.member.model.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentRequest {

    private Long id;
    private String content;
    private Member writer;
    private Item item;
}
