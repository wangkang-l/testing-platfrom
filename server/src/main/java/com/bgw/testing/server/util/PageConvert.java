package com.bgw.testing.server.util;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

public class PageConvert {

    public static <T> PageInfo<T> getPageInfo(Page page, List<T> list) {
        PageInfo<T> pageInfo = page.toPageInfo();
        pageInfo.setList(list);
        return pageInfo;
    }

}
