package hypermart.hypermart.uploadFile.service;

import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.item.repository.ItemRepository;
import hypermart.hypermart.uploadFile.dto.UploadFileRequest;
import hypermart.hypermart.uploadFile.dto.UploadFileResponse;
import hypermart.hypermart.uploadFile.model.UploadFile;
import hypermart.hypermart.uploadFile.repository.UploadFileRepository;
import hypermart.hypermart.uploadFile.util.UploadFileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UploadFileService {

    private final UploadFileRepository uploadFileRepository;
    private final ItemRepository itemRepository;

    public List<UploadFileResponse> getFiles(Item item) {
        return UploadFileMapper.entityToDtoList(
                uploadFileRepository.findFilesByItem(item)
        );
    }

    @Transactional
    public void saveFile(List<MultipartFile> uploadFile, Long itemId) throws IOException {
        Item item = itemRepository.findOneById(itemId);
        for (MultipartFile file : uploadFile) {
            String saveFileName = makeSaveFileName(file);
            file.transferTo(new File(saveFileName));

            UploadFileRequest dto = UploadFileRequest.builder()
                    .saveFileName(saveFileName)
                    .item(item)
                    .build();
            uploadFileRepository.save(UploadFileMapper.dtoToEntity(dto));
        }
    }

    private String makeSaveFileName(MultipartFile file) {
        UUID uuid = UUID.randomUUID();
        return uuid + "_" + file.getOriginalFilename();
    }

    @Transactional
    public void editFile(List<MultipartFile> uploadFile, Item item) throws IOException {
        deleteFile(item);
        saveFile(uploadFile, item.getId());
    }

    @Transactional
    public void deleteFile(Item item) {
        List<UploadFile> files = uploadFileRepository.findFilesByItem(item);

        for (UploadFile uploadFile : files) {
            String saveFileName = uploadFile.getSaveFileName();
            File file = new File("C:\\Temp\\upload\\" + saveFileName);
            if (file.delete()) {
                log.info("file : " + saveFileName + " 삭제 완료");
            }
        }
        uploadFileRepository.deleteBulkFileByItem(item);
    }
}
