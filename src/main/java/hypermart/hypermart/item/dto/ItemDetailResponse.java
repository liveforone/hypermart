package hypermart.hypermart.item.dto;

import hypermart.hypermart.uploadFile.dto.UploadFileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDetailResponse {

    private String user;
    private ItemResponse item;
    private List<UploadFileResponse> files;
}
