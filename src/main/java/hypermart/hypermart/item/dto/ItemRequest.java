package hypermart.hypermart.item.dto;

import hypermart.hypermart.member.model.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemRequest {

    private Long id;
    private String title;
    private String content;
    private String category;
    private int price;
    private int remaining;
    private int good;
    private Member writer;
}
