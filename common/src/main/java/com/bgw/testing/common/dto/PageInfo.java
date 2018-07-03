package com.bgw.testing.common.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Valid
public class PageInfo<T> {

    private Integer total;
    private List<T> data;

    public PageInfo() {}

    public PageInfo(@NotNull List<T> list, int pageNum, int pageSize) {
        if (pageNum <= 0) {
            pageNum = 1;
        }
        if (pageSize <= 0) {
            pageSize = 10;
        }

        int fromIndex = (pageNum-1)*pageSize;
        int toIndex = pageNum*pageSize;
        this.total = list.size();
        this.data = list.subList(fromIndex >= total ? total : fromIndex, toIndex >= total ? total : toIndex);
    }

    public PageInfo(@NotNull List<T> list) {
        int fromIndex = 0;
        this.total = list.size();
        this.data = list.subList(fromIndex,total);
    }
}
