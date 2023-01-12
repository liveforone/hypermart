package hypermart.hypermart.item.controller;

import hypermart.hypermart.item.dto.ItemRequest;
import hypermart.hypermart.item.dto.ItemResponse;
import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.item.service.ItemService;
import hypermart.hypermart.item.util.ItemMapper;
import hypermart.hypermart.uploadFile.dto.UploadFileResponse;
import hypermart.hypermart.uploadFile.service.UploadFileService;
import hypermart.hypermart.utility.CommonUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private final UploadFileService uploadFileService;

    @GetMapping("/item")
    public ResponseEntity<Page<ItemResponse>> itemHome(
            @PageableDefault(page = 0, size = 10)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "good", direction = Sort.Direction.DESC),
                    @SortDefault(sort = "id", direction = Sort.Direction.DESC)
            }) Pageable pageable
    ) {
        Page<ItemResponse> items = itemService.getItems(pageable);

        return ResponseEntity.ok(items);
    }

    @GetMapping("/item/search-title")
    public ResponseEntity<Page<ItemResponse>> itemTitleSearch(
            @RequestParam String title,
            @PageableDefault(page = 0, size = 10)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "good", direction = Sort.Direction.DESC),
                    @SortDefault(sort = "id", direction = Sort.Direction.DESC)
            }) Pageable pageable
    ) {
        Page<ItemResponse> items = itemService.searchItemsByTitle(title, pageable);

        return ResponseEntity.ok(items);
    }

    @GetMapping("/item/search-category")
    public ResponseEntity<Page<ItemResponse>> itemCategorySearch(
            @RequestParam String category,
            @PageableDefault(page = 0, size = 10)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "good", direction = Sort.Direction.DESC),
                    @SortDefault(sort = "id", direction = Sort.Direction.DESC)
            }) Pageable pageable
    ) {
        Page<ItemResponse> items = itemService.searchItemsByCategory(category, pageable);

        return ResponseEntity.ok(items);
    }

    @GetMapping("/item/{id}")
    public ResponseEntity<?> itemDetail(
            @PathVariable("id") Long id,
            Principal principal
    ) {
        Item item = itemService.getItemDetail(id);

        if (CommonUtils.isNull(item)) {
            return ResponseEntity.ok("존재하지 않는 상품입니다.");
        }

        String email = principal.getName();
        List<UploadFileResponse> files = uploadFileService.getFiles(item);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user", email);
        hashMap.put("item", ItemMapper.entityToDtoDetail(item));
        hashMap.put("files", files);

        return ResponseEntity.ok(hashMap);
    }

    @GetMapping("/item/post")
    public ResponseEntity<?> itemPostPage() {
        return ResponseEntity.ok("상품 등록 페이지");
    }

    @PostMapping("/item/post")
    public ResponseEntity<?> itemPost(
            @RequestPart ItemRequest itemRequest,
            @RequestPart List<MultipartFile> uploadFile,
            Principal principal,
            HttpServletRequest request
    ) throws IllegalStateException, IOException {
        if (CommonUtils.isEmptyMultipartFile(uploadFile)) {
            log.info("파일 없음.");
            return ResponseEntity.ok("파일이 없으면 상품 등록이 불가능합니다.");
        }

        String email = principal.getName();
        Long itemId = itemService.savePost(itemRequest, email);
        log.info("상품 등록 성공 id = " + itemId);
        uploadFileService.saveFile(uploadFile, itemId);
        log.info("파일 등록 성공");

        String url = "/item/" + itemId;
        return CommonUtils.makeResponseEntityForRedirect(url, request);
    }
}
