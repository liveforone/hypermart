package hypermart.hypermart.comment.service;

import hypermart.hypermart.comment.dto.CommentRequest;
import hypermart.hypermart.comment.dto.CommentResponse;
import hypermart.hypermart.comment.model.Comment;
import hypermart.hypermart.comment.repository.CommentRepository;
import hypermart.hypermart.comment.util.CommentMapper;
import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.member.repository.MemberRepository;
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
    private final MemberRepository memberRepository;

    public Comment getCommentDetail(Long id) {
        return commentRepository.findOneById(id);
    }

    public Page<CommentResponse> getCommentsByItem(Item item, Pageable pageable) {
        return CommentMapper.entityToDtoPage(
                commentRepository.findCommentsByItem(item, pageable)
        );
    }

    @Transactional
    public void saveComment(CommentRequest commentRequest, Item item, String email) {
        commentRequest.setItem(item);
        commentRequest.setWriter(memberRepository.findByEmail(email));

        commentRepository.save(
                CommentMapper.dtoToEntity(commentRequest)
        );
    }

    @Transactional
    public void editComment(String content, Long id) {
        commentRepository.editContent(content, id);
    }

    @Transactional
    public void deleteCommentById(Long id) {
        commentRepository.deleteById(id);
    }

    @Transactional
    public void deleteCommentsByItem(Item item) {
        commentRepository.deleteBulkCommentsByItem(item);
    }
}
