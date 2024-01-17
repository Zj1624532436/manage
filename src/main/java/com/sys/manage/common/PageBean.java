package com.sys.manage.common;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class PageBean {

    @Getter@Setter
    private int pageNum;//第几页
    @Getter@Setter
    private int pageSize;//每页记录数
    @Setter
    private int start;//开始那条
    @Getter@Setter
    private String query;//查询参数

    public int getStart(){
        return (pageNum-1)*pageSize;
    }
}
