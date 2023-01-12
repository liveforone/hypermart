package hypermart.hypermart.item.model;

import hypermart.hypermart.member.model.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    private String category;

    @Column(nullable = false)
    private int price;

    private int remaining;

    private int good;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Member writer;

    @Builder
    public Item(Long id, String title, String content, String category, int price, int remaining, int good, Member writer) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.price = price;
        this.remaining = remaining;
        this.good = good;
        this.writer = writer;
    }
}
