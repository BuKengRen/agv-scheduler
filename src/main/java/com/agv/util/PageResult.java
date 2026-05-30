package com.agv.util;

import com.agv.entity.Agv;
import lombok.Data;

import java.util.List;
@Data
public class PageResult {
    private Long total;      // 总记录数
    private List<?> list;    // 当前页数据

    public PageResult(Long total, List<Agv> list) {
        this.total = total;
        this.list = list;
    }
}