package jpabook.jpashop.service;

import jpabook.jpashop.domain.Item.Book;
import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    /**
     * 변경 감지 기능 사용 (제일 권장)
     */
    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) { // 여기서 itemId는 준영속 상태
        // 값을 찾아와 영속 상태로 만들어 줌
        Item findItem = itemRepository.findOne(itemId);
        // 파라미터가 너무 많아지면, DTO를 통해 구현하는 방법도 좋음
        // 사실 아래와 같이 setter를 사용하면 안되고(추적이 어려움), changeItem과 같은 메서드를 만들어 구현하는 것이 권장됨
        findItem.setPrice(price);
        findItem.setName(name);
        findItem.setStockQuantity(stockQuantity);
        // commit, flush
    }

    @Transactional(readOnly = true)
    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
