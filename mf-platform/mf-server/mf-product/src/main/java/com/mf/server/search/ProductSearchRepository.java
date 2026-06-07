package com.mf.server.search;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, Long> {
    List<ProductDocument> findByNameContainingOrDescriptionContaining(String name, String desc);
    List<ProductDocument> findByProductType(String productType);
}
