package hypermart.hypermart.item.repository;

import hypermart.hypermart.item.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select i from Item i join i.writer")
    Page<Item> findAllItems(Pageable pageable);

    @Query("select i from Item i join i.writer where i.title like %:title%")
    Page<Item> searchByTitle(@Param("title") String title, Pageable pageable);

    @Query("select i from Item i join i.writer where i.category like %:category%")
    Page<Item> searchByCategory(@Param("category") String category, Pageable pageable);

    @Query("select i from Item i join fetch i.writer w where w.email = :email")
    List<Item> findItemsByWriter(@Param("email") String email);

    @Query("select i from Item i join fetch i.writer where i.id = :id")
    Item findOneById(@Param("id") Long id);

    @Modifying
    @Query("update Item i set i.content = :content where i.id = :id")
    void updateContent(@Param("content") String content, @Param("id") Long id);

    @Modifying
    @Query("update Item i set i.remaining = :remaining where i.id = :id")
    void restockItem(@Param("remaining") int remaining, @Param("id") Long id);

    @Modifying
    @Query("update Item i set i.remaining = i.remaining - :orderQuantity where i.id = :id")
    void decreaseMultipleRemaining(@Param("orderQuantity") int orderQuantity, @Param("id") Long id);

    @Modifying
    @Query("update Item i set i.remaining = i.remaining - 1 where i.id = :id")
    void decreaseSingleRemaining(@Param("id") Long id);

    @Modifying
    @Query("update Item i set i.remaining = i.remaining + 1 where i.id = :id")
    void increaseSingleRemaining(@Param("id") Long id);

    @Modifying
    @Query("update Item i set i.good = i.good + 1 where i.id = :id")
    void increaseGood(@Param("id") Long id);
}
