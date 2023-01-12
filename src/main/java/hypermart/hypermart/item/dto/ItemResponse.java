package hypermart.hypermart.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponse {

    private Long id;
    private String title;
    private String content;
    private String category;
    private String writer;
    private int price;
    private int remaining;
    private int good;
}
