package com.mf.common.base;
import lombok.AllArgsConstructor; import lombok.Data; import lombok.NoArgsConstructor;
import java.io.Serializable; import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class PageVO<T> implements Serializable {
    private Long total; private Integer page; private Integer size; private List<T> records;
    public static <T> PageVO<T> of(Long total, Integer page, Integer size, List<T> records) {
        return new PageVO<>(total, page, size, records);
    }
}
