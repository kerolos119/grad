package org.example.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
// يضيف مُنشئًا بجميع الحقول
public class PageResult<T> {
    private Long totalElement;
    private List<T> items;
    private int totalPages;
    private int currentPage;
    private int pageSize;


    // المُنشئ في PageResult
    public PageResult(List<T> items, Long totalElements, int totalPages, int currentPage, int pageSize) {
        this.items = items;
        this.totalElement = totalElements;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }
}
