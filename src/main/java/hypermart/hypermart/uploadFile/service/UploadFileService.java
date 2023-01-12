package hypermart.hypermart.uploadFile.service;

import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.item.repository.ItemRepository;
import hypermart.hypermart.uploadFile.dto.UploadFileRequest;
import hypermart.hypermart.uploadFile.dto.UploadFileResponse;
import hypermart.hypermart.uploadFile.repository.UploadFileRepository;
import hypermart.hypermart.uploadFile.util.UploadFileMapper;
import lombok.RequiredArgsConstructor;
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
}
