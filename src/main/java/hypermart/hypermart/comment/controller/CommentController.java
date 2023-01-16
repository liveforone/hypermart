package hypermart.hypermart.comment.controller;

import hypermart.hypermart.comment.dto.CommentResponse;
import hypermart.hypermart.comment.service.CommentService;
import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.item.service.ItemService;
import hypermart.hypermart.utility.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;
    private final ItemService itemService;

    @GetMapping("/comment/{itemId}")
    public ResponseEntity<?> commentsPage(
            @PathVariable("itemId") Long itemId,
            @PageableDefault(page = 0, size = 10)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "id", direction = Sort.Direction.DESC)
            }) Pageable pageable,
            Principal principal
    ) {
        Item item = itemService.getItemDetail(itemId);
        if (CommonUtils.isNull(item)) {
            return ResponseEntity.ok("존재하지 않는 상품입니다.");
        }

        Page<CommentResponse> comments = commentService.getCommentsByItem(item, pageable);
        String email = principal.getName();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user", email);
        hashMap.put("comments", comments);

        return ResponseEntity.ok(hashMap);
    }
}
