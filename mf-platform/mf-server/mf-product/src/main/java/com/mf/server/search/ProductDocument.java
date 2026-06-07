package com.mf.server.search;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.math.BigDecimal;

@Data
@Document(indexName = "products")
public class ProductDocument {
    @Id private Long id;
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String name;
    @Field(type = FieldType.Keyword) private String productType;
    @Field(type = FieldType.Keyword) private Long categoryId;
    @Field(type = FieldType.Text, analyzer = "ik_max_word") private String brand;
    @Field(type = FieldType.Keyword) private String coverImage;
    @Field(type = FieldType.Double) private BigDecimal price;
    @Field(type = FieldType.Double) private BigDecimal originalPrice;
    @Field(type = FieldType.Integer) private Integer stock;
    @Field(type = FieldType.Keyword) private String unit;
    @Field(type = FieldType.Integer) private Integer salesCount;
    @Field(type = FieldType.Integer) private Integer status;
    @Field(type = FieldType.Integer) private Integer isRecommend;
    @Field(type = FieldType.Integer) private Integer isNew;
    @Field(type = FieldType.Text, analyzer = "ik_max_word") private String description;
}
