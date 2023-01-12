package hypermart.hypermart.uploadFile.dto;

import hypermart.hypermart.item.model.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileRequest {

    private Long id;
    private String saveFileName;
    private Item item;
}
