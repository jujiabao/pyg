package com.pyg.utils;

import java.io.Serializable;
import java.util.List;

/**
 * @Title PageResult
 * @ProjectName pyg
 * @Description
 * @Author Hello.Ju
 * @Date 2019-07-17 22:10
 */
public class PageResult implements Serializable {
    //mybatis的分页插件 pagehelper插件
    //总记录数
    private Long total;
    //总记录
    private List<?> rows;

    public PageResult(Long total, List<?> rows) {
        this.total = total;
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "PageResult{" +
                "total=" + total +
                ", rows=" + rows +
                '}';
    }
}
