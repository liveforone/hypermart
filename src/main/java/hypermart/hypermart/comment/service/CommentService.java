package hypermart.hypermart.comment.service;

import hypermart.hypermart.comment.dto.CommentResponse;
import hypermart.hypermart.comment.repository.CommentRepository;
import hypermart.hypermart.comment.util.CommentMapper;
import hypermart.hypermart.item.model.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;

    public Page<CommentResponse> getCommentsByItem(Item item, Pageable pageable) {
        return CommentMapper.entityToDtoPage(
                commentRepository.findCommentsByItem(item, pageable)
        );
    }
}
