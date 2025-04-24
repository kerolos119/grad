package org.example.dto;

import org.springframework.data.domain.Page;

public class PageUtils {
    /**
     * يحول Page<T> إلى PageResult<T> باستخدام البناء الموجود في DTO.
     */
    public static <T> PageResult<T> toPageResult(Page<T> page) {
        return new PageResult<>(
                page.getContent(),            // قائمة العناصر
                page.getTotalElements(),      // إجمالي عدد العناصر
                page.getTotalPages(),         // إجمالي الصفحات
                page.getNumber(),             // الصفحة الحالية (0-based)
                page.getSize()                // حجم الصفحة
        );
    }
}
