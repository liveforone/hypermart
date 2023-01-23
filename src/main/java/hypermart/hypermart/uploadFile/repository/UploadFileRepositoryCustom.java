package hypermart.hypermart.uploadFile.repository;

import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.uploadFile.model.UploadFile;

import java.util.List;

public interface UploadFileRepositoryCustom {

    List<UploadFile> findFilesByItem(Item item);

    void deleteBulkFileByItem(Item item);
}
