package com.sys.manage.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
public class PageBean {

    @Getter
    private int pageNum;//第几页
    @Getter
    private int pageSize;//每页记录数
    private int start;//开始那条
    @Getter
    private String query;//查询参数

    public int getStart(){
        return (pageNum-1)*pageSize;
    }
}
