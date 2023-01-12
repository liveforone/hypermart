package hypermart.hypermart.uploadFile.model;

import hypermart.hypermart.item.model.Item;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UploadFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String saveFileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Builder
    public UploadFile(Long id, String saveFileName, Item item) {
        this.id = id;
        this.saveFileName = saveFileName;
        this.item = item;
    }
}
