package com.mf.server.search;
import com.mf.server.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service @RequiredArgsConstructor
public class ProductSearchService {
    private final ProductSearchRepository repository;

    /** 索引单个商品 */
    public void index(Product p) {
        var d = new ProductDocument();
        copy(p, d);
        repository.save(d);
    }

    /** 全量重建索引 */
    public void indexAll(List<Product> products) {
        var docs = products.stream().map(p -> {
            var d = new ProductDocument();
            copy(p, d);
            return d;
        }).toList();
        repository.saveAll(docs);
    }

    /** ES 分词搜索，返回匹配的商品 ID 列表 */
    public List<Long> search(String keyword) {
        return repository.findByNameContainingOrDescriptionContaining(keyword, keyword)
                .stream().map(ProductDocument::getId).toList();
    }

    public void remove(Long id) { repository.deleteById(id); }

    public void rebuild(List<Product> all) { repository.deleteAll(); indexAll(all); }

    private void copy(Product p, ProductDocument d) {
        d.setId(p.getId()); d.setName(p.getName()); d.setProductType(p.getProductType());
        d.setCategoryId(p.getCategoryId()); d.setBrand(p.getBrand());
        d.setCoverImage(p.getCoverImage()); d.setPrice(p.getPrice());
        d.setOriginalPrice(p.getOriginalPrice()); d.setStock(p.getStock());
        d.setUnit(p.getUnit()); d.setSalesCount(p.getSalesCount());
        d.setStatus(p.getStatus()); d.setIsRecommend(p.getIsRecommend());
        d.setIsNew(p.getIsNew()); d.setDescription(p.getDescription());
    }
}
