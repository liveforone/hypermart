package hypermart.hypermart.comment.controller;

import hypermart.hypermart.comment.dto.CommentRequest;
import hypermart.hypermart.comment.dto.CommentResponse;
import hypermart.hypermart.comment.dto.CommentsPageResponse;
import hypermart.hypermart.comment.model.Comment;
import hypermart.hypermart.comment.service.CommentService;
import hypermart.hypermart.comment.util.CommentMapper;
import hypermart.hypermart.comment.util.CommentUtils;
import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.item.service.ItemService;
import hypermart.hypermart.member.model.Member;
import hypermart.hypermart.member.service.MemberService;
import hypermart.hypermart.orders.model.Orders;
import hypermart.hypermart.orders.service.OrdersService;
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

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;
    private final ItemService itemService;
    private final MemberService memberService;
    private final OrdersService ordersService;

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
        CommentsPageResponse commentsPageResponse =
                CommentMapper.createCommentsPageResponse(comments, email);

        return ResponseEntity.ok(commentsPageResponse);
    }

    @PostMapping("/comment/post/{itemId}")
    public ResponseEntity<?> postComment(
            @PathVariable("itemId") Long itemId,
            @RequestBody CommentRequest commentRequest,
            Principal principal,
            HttpServletRequest request
    ) {
        Item item = itemService.getItemDetail(itemId);
        if (CommonUtils.isNull(item)) {
            return ResponseEntity.ok("존재하지 않는 상품입니다.");
        }

        String email = principal.getName();
        Member member = memberService.getMemberEntity(email);
        List<Orders> orders = ordersService.getOrdersByMemberAndItem(member, item);
        if (CommonUtils.isNull(orders)) {
            return ResponseEntity.ok("리뷰는 주문한 상품만 가능합니다.");
        }

        Comment comment = commentService.getCommentDetailByWriter(member);
        if (CommentUtils.isDuplicateComment(comment)) {
            return ResponseEntity.ok("이미 리뷰를 작성하셨습니다.\n리뷰는 한 번만 작성가능합니다.");
        }

        commentService.saveComment(commentRequest, item, member);
        log.info("리뷰 등록 성공");

        String url = "/comment/" + itemId;
        return CommonUtils.makeResponseEntityForRedirect(url, request);
    }

    @PatchMapping("/comment/edit/{commentId}")
    public ResponseEntity<?> editComment(
            @PathVariable("commentId") Long commentId,
            @RequestBody String content,
            Principal principal
    ) {
        Comment comment = commentService.getCommentDetailById(commentId);
        if (CommonUtils.isNull(comment)) {
            return ResponseEntity.ok("존재하지 않는 리뷰입니다.");
        }

        String email = principal.getName();
        String writer = comment.getWriter().getEmail();
        if (!Objects.equals(email, writer)) {
            return ResponseEntity.ok("작성자가 아니면 수정이 불가능합니다.");
        }

        commentService.editComment(content, commentId);
        log.info("리뷰 수정성공");

        return ResponseEntity.ok("리뷰를 성공적으로 수정했습니다.");
    }

    @DeleteMapping("/comment/delete/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable("commentId") Long commentId,
            Principal principal
    ) {
        Comment comment = commentService.getCommentDetailById(commentId);
        if (CommonUtils.isNull(comment)) {
            return ResponseEntity.ok("존재하지 않는 리뷰입니다.");
        }

        String email = principal.getName();
        String writer = comment.getWriter().getEmail();
        if (!Objects.equals(email, writer)) {
            return ResponseEntity.ok("작성자가 아니면 삭제가 불가능합니다.");
        }

        commentService.deleteCommentById(commentId);
        log.info("리뷰 삭제 성공");

        return ResponseEntity.ok("리뷰를 성공적으로 삭제했습니다.");
    }
}
