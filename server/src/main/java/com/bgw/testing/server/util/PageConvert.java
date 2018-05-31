package com.bgw.testing.server.util;

import com.bgw.testing.common.dto.PageInfo;
import com.github.pagehelper.Page;

import java.util.List;

public class PageConvert {

    public static <T> PageInfo<T> getPageInfo(Page page, List<T> list) {
        PageInfo<T> pageInfo = new PageInfo<>();
        pageInfo.setData(list);
        pageInfo.setTotal(Math.toIntExact(page.getTotal()));
        return pageInfo;
    }

}
