package hypermart.hypermart.item.controller;

import hypermart.hypermart.basket.service.BasketService;
import hypermart.hypermart.comment.service.CommentService;
import hypermart.hypermart.item.dto.ItemDetailResponse;
import hypermart.hypermart.item.dto.ItemRequest;
import hypermart.hypermart.item.dto.ItemResponse;
import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.item.service.ItemService;
import hypermart.hypermart.item.util.ItemMapper;
import hypermart.hypermart.orders.service.OrdersService;
import hypermart.hypermart.recommend.service.RecommendationService;
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
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private final UploadFileService uploadFileService;
    private final CommentService commentService;
    private final RecommendationService recommendationService;
    private final BasketService basketService;
    private final OrdersService ordersService;

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
    public ResponseEntity<Page<ItemResponse>> itemTitleSearchPage(
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
    public ResponseEntity<Page<ItemResponse>> itemCategorySearchPage(
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
            return ResponseEntity.ok("???????????? ?????? ???????????????.");
        }

        String email = principal.getName();
        List<UploadFileResponse> files = uploadFileService.getFiles(item);
        ItemDetailResponse itemDetailResponse =
                ItemMapper.createItemDetailResponse(email, item, files);

        return ResponseEntity.ok(itemDetailResponse);
    }

    @GetMapping("/seller-page")
    public ResponseEntity<List<ItemResponse>> sellerPage(Principal principal) {
        String email = principal.getName();
        List<ItemResponse> items = itemService.getItemsByWriter(email);

        return ResponseEntity.ok(items);
    }

    @GetMapping("/item/post")
    public ResponseEntity<?> itemPostPage() {
        return ResponseEntity.ok("?????? ?????? ?????????");
    }

    @PostMapping("/item/post")
    public ResponseEntity<?> postItem(
            @RequestPart ItemRequest itemRequest,
            @RequestPart List<MultipartFile> uploadFile,
            Principal principal,
            HttpServletRequest request
    ) throws IllegalStateException, IOException {
        if (CommonUtils.isEmptyMultipartFile(uploadFile)) {
            log.info("?????? ??????.");
            return ResponseEntity.ok("????????? ????????? ?????? ????????? ??????????????????.");
        }

        String email = principal.getName();
        Long itemId = itemService.savePost(itemRequest, email);
        log.info("?????? ?????? ?????? id = " + itemId);
        uploadFileService.saveFile(uploadFile, itemId);
        log.info("?????? ?????? ??????");

        String url = "/item/" + itemId;
        return CommonUtils.makeResponseEntityForRedirect(url, request);
    }

    @GetMapping("/item/edit/{id}")
    public ResponseEntity<?> itemEditPage(
            @PathVariable("id") Long id,
            Principal principal
    ) {
        Item item = itemService.getItemDetail(id);
        if (CommonUtils.isNull(item)) {
            return ResponseEntity.ok("???????????? ?????? ???????????????.");
        }

        String email = principal.getName();
        String writer = item.getWriter().getEmail();
        if (!Objects.equals(email, writer)) {
            return ResponseEntity.ok("???????????? ????????? ??? ????????????.");
        }

        return ResponseEntity.ok(ItemMapper.entityToDtoDetail(item));
    }

    @PatchMapping("/item/edit/{id}")
    public ResponseEntity<?> editItem(
            @PathVariable("id") Long id,
            @RequestPart String content,
            @RequestPart List<MultipartFile> uploadFile,
            Principal principal
    ) throws IOException {
        Item item = itemService.getItemDetail(id);
        if (CommonUtils.isNull(item)) {
            return ResponseEntity.ok("???????????? ?????? ???????????????.");
        }

        String email = principal.getName();
        String writer = item.getWriter().getEmail();
        if (!Objects.equals(email, writer)) {
            return ResponseEntity.ok("???????????? ????????? ??? ????????????.");
        }

        itemService.updateContent(content, id);
        log.info("?????? ?????? ??????");

        if (!CommonUtils.isEmptyMultipartFile(uploadFile)) {
            uploadFileService.editFile(uploadFile, item);
            log.info("?????? ?????? ??????");
            return ResponseEntity.ok("????????? ??????????????? ?????????????????????.");
        }

        return ResponseEntity.ok("????????? ??????????????? ?????????????????????.");
    }

    @PatchMapping("/item/update-remaining/{id}")
    public ResponseEntity<?> restockItem(
            @PathVariable("id") Long id,
            @RequestBody int remaining,
            Principal principal
    ) {
        Item item = itemService.getItemDetail(id);
        if (CommonUtils.isNull(item)) {
            return ResponseEntity.ok("???????????? ?????? ???????????????.");
        }

        String email = principal.getName();
        String writer = item.getWriter().getEmail();
        if (!Objects.equals(email, writer)) {
            return ResponseEntity.ok("???????????? ????????? ???????????? ??? ??? ????????????.");
        }

        itemService.restockItem(remaining, id);
        log.info("?????? ???????????? ??????");

        return ResponseEntity.ok("?????? ??????????????? ??????????????? ?????????????????????.");
    }

    @DeleteMapping("/item/delete/{id}")
    public ResponseEntity<?> deleteItem(
            @PathVariable("id") Long id,
            Principal principal
    ) {
        Item item = itemService.getItemDetail(id);
        if (CommonUtils.isNull(item)) {
            return ResponseEntity.ok("???????????? ?????? ???????????????.");
        }

        String email = principal.getName();
        String writer = item.getWriter().getEmail();
        if (!Objects.equals(email, writer)) {
            return ResponseEntity.ok("???????????? ????????? ??? ????????????.");
        }

        uploadFileService.deleteFile(item);
        log.info("?????? ?????? ?????? ?????? ??????");
        commentService.deleteCommentsByItem(item);
        log.info("?????? ?????? ?????? ?????? ??????");
        recommendationService.deleteRecommendationsByItem(item);
        log.info("?????? ?????? ?????? ?????? ??????");
        basketService.deleteBasketsByItem(item);
        log.info("?????? ???????????? ?????? ?????? ??????");
        ordersService.deleteOrdersByItem(item);
        log.info("?????? ?????? ?????? ?????? ??????");
        itemService.deleteItem(id);
        log.info("?????? ?????? ??????");

        return ResponseEntity.ok("????????? ??????????????? ?????????????????????.");
    }
}
