package hypermart.hypermart.uploadFile.util;

import hypermart.hypermart.uploadFile.dto.UploadFileRequest;
import hypermart.hypermart.uploadFile.dto.UploadFileResponse;
import hypermart.hypermart.uploadFile.model.UploadFile;

import java.util.List;
import java.util.stream.Collectors;

public class UploadFileMapper {

    public static UploadFile dtoToEntity(UploadFileRequest uploadFileRequest) {
        return UploadFile.builder()
                .id(uploadFileRequest.getId())
                .saveFileName(uploadFileRequest.getSaveFileName())
                .item(uploadFileRequest.getItem())
                .build();
    }

    private static UploadFileResponse dtoBuilder(UploadFile uploadFile) {
        return UploadFileResponse.builder()
                .id(uploadFile.getId())
                .saveFileName(uploadFile.getSaveFileName())
                .build();
    }

    public static List<UploadFileResponse> entityToDtoList(List<UploadFile> uploadFiles) {
        return uploadFiles.stream()
                .map(UploadFileMapper::dtoBuilder)
                .collect(Collectors.toList());
    }
}
