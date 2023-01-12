package hypermart.hypermart.uploadFile.repository;

import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.uploadFile.model.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {

    @Query("select u from UploadFile u join fetch u.item where u.item = :item")
    List<UploadFile> findFilesByItem(@Param("item") Item item);

    @Modifying(clearAutomatically = true)
    @Query("delete from UploadFile u where u.item = :item")
    void deleteBulkFileByItem(@Param("item") Item item);
}
