package org.keniding.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse<T> {
    private List<T> content;
    private long totalElements;
    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private boolean last;
    private boolean first;

    public static <T> PagedResponse<T> of(List<T> content, long totalElements, int pageNumber, int pageSize) {
        PagedResponse<T> response = new PagedResponse<>();
        response.setContent(content);
        response.setTotalElements(totalElements);
        response.setPageNumber(pageNumber);
        response.setPageSize(pageSize);

        int totalPages = pageSize > 0 ? (int) Math.ceil((double) totalElements / pageSize) : 0;
        response.setTotalPages(totalPages);
        response.setLast(pageNumber >= totalPages - 1);
        response.setFirst(pageNumber == 0);

        return response;
    }
}
