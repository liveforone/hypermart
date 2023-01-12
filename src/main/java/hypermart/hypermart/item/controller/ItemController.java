package hypermart.hypermart.item.controller;

import hypermart.hypermart.item.dto.ItemResponse;
import hypermart.hypermart.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;

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
}
