package com.sys.manage.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName address
 */
@TableName(value ="address")
@Data
public class Address implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    @TableField(value = "address")
    private String address;

    /**
     * 
     */
    @TableField(value = "text")
    private String text;

    /**
     * 
     */
    @TableField(value = "url",updateStrategy = FieldStrategy.IGNORED)
    private String url;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public void urlToNull(){
        this.url = null;
    }
}