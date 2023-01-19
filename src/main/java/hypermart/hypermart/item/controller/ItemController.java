package hypermart.hypermart.item.controller;

import hypermart.hypermart.basket.service.BasketService;
import hypermart.hypermart.comment.service.CommentService;
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
import java.util.HashMap;
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

    @GetMapping("/seller-page")
    public ResponseEntity<List<ItemResponse>> sellerPage(Principal principal) {
        String email = principal.getName();
        List<ItemResponse> items = itemService.getItemsByWriter(email);

        return ResponseEntity.ok(items);
    }

    @GetMapping("/item/post")
    public ResponseEntity<?> itemPostPage() {
        return ResponseEntity.ok("상품 등록 페이지");
    }

    @PostMapping("/item/post")
    public ResponseEntity<?> postItem(
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

    @GetMapping("/item/edit/{id}")
    public ResponseEntity<?> itemEditPage(
            @PathVariable("id") Long id,
            Principal principal
    ) {
        Item item = itemService.getItemDetail(id);
        if (CommonUtils.isNull(item)) {
            return ResponseEntity.ok("존재하지 않는 상품입니다.");
        }

        String email = principal.getName();
        String writer = item.getWriter().getEmail();
        if (!Objects.equals(email, writer)) {
            return ResponseEntity.ok("작성자만 수정할 수 있습니다.");
        }

        return ResponseEntity.ok(ItemMapper.entityToDtoDetail(item));
    }

    @PutMapping("/item/edit/{id}")
    public ResponseEntity<?> editItem(
            @PathVariable("id") Long id,
            @RequestPart String content,
            @RequestPart List<MultipartFile> uploadFile,
            Principal principal
    ) throws IOException {
        Item item = itemService.getItemDetail(id);
        if (CommonUtils.isNull(item)) {
            return ResponseEntity.ok("존재하지 않는 상품입니다.");
        }

        String email = principal.getName();
        String writer = item.getWriter().getEmail();
        if (!Objects.equals(email, writer)) {
            return ResponseEntity.ok("작성자만 수정할 수 있습니다.");
        }

        itemService.updateContent(content, id);
        log.info("상품 수정 성공");

        if (!CommonUtils.isEmptyMultipartFile(uploadFile)) {
            uploadFileService.editFile(uploadFile, item);
            log.info("파일 수정 성공");
            return ResponseEntity.ok("상품을 성공적으로 수정하였습니다.");
        }

        return ResponseEntity.ok("상품을 성공적으로 수정하였습니다.");
    }

    @PutMapping("/item/update-remaining/{id}")
    public ResponseEntity<?> restockItem(
            @PathVariable("id") Long id,
            @RequestBody int remaining,
            Principal principal
    ) {
        Item item = itemService.getItemDetail(id);
        if (CommonUtils.isNull(item)) {
            return ResponseEntity.ok("존재하지 않는 상품입니다.");
        }

        String email = principal.getName();
        String writer = item.getWriter().getEmail();
        if (!Objects.equals(email, writer)) {
            return ResponseEntity.ok("작성자만 재고를 업데이트 할 수 있습니다.");
        }

        itemService.restockItem(remaining, id);
        log.info("재고 업데이트 성공");

        return ResponseEntity.ok("재고 업데이트를 성공적으로 완료하였습니다.");
    }

    @DeleteMapping("/item/delete/{id}")
    public ResponseEntity<?> deleteItem(
            @PathVariable("id") Long id,
            Principal principal
    ) {
        Item item = itemService.getItemDetail(id);
        if (CommonUtils.isNull(item)) {
            return ResponseEntity.ok("존재하지 않는 상품입니다.");
        }

        String email = principal.getName();
        String writer = item.getWriter().getEmail();
        if (!Objects.equals(email, writer)) {
            return ResponseEntity.ok("작성자만 수정할 수 있습니다.");
        }

        uploadFileService.deleteFile(item);
        log.info("연관 파일 전체 삭제 성공");
        commentService.deleteCommentsByItem(item);
        log.info("연관 리뷰 전체 삭제 성공");
        recommendationService.deleteRecommendationsByItem(item);
        log.info("연관 추천 전체 삭제 성공");
        basketService.deleteBasketsByItem(item);
        log.info("연관 장바구니 전체 삭제 성공");
        ordersService.deleteOrdersByItem(item);
        log.info("연관 주문 전체 삭제 성공");
        itemService.deleteItem(id);
        log.info("상품 삭제 성공");

        return ResponseEntity.ok("상품을 성공적으로 삭제하였습니다.");
    }
}
