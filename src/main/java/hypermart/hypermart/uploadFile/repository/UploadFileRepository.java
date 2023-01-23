package hypermart.hypermart.uploadFile.repository;

import hypermart.hypermart.uploadFile.model.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long>, UploadFileRepositoryCustom {
}
