package hypermart.hypermart.uploadFile.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.item.model.QItem;
import hypermart.hypermart.uploadFile.model.QUploadFile;
import hypermart.hypermart.uploadFile.model.UploadFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UploadFileRepositoryImpl implements UploadFileRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public List<UploadFile> findFilesByItem(Item item) {
        QUploadFile uploadFile = QUploadFile.uploadFile;
        QItem qitem = QItem.item;

        return queryFactory.selectFrom(uploadFile)
                .join(uploadFile.item, qitem)
                .on(qitem.eq(item))
                .orderBy(uploadFile.id.asc())
                .fetch();
    }

    public void deleteBulkFileByItem(Item item) {
        QUploadFile uploadFile = QUploadFile.uploadFile;

        queryFactory.delete(uploadFile)
                .where(uploadFile.item.eq(item))
                .execute();
    }
}
