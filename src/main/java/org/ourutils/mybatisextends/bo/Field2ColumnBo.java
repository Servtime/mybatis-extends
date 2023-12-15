package org.ourutils.mybatisextends.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 *******************************************************************************
 *    系统名称   ： mybatis-extends项目
 *    客户      ：  *
 *    文件名    ： org.ourutils.mybatisextends.bo.Field2ColumnBo.java
 *              (C) Copyright 2023 our-utils Corporation
 *               All Rights Reserved.
 * *****************************************************************************
 * <PRE>
 * 作用
 *       列信息和属性信息封装类
 * 限制
 *       无。
 * 注意事项
 *       无。
 * 修改历史
 * -----------------------------------------------------------------------------
 *         VERSION       DATE            @author       CHANGE/COMMENT
 * -----------------------------------------------------------------------------
 *         1.0.0        2023/12/5          wsil      create
 * -----------------------------------------------------------------------------
 * </PRE>
 **/
@Data
@AllArgsConstructor
public class Field2ColumnBo implements Serializable {

    /**
     * 列名称 @Column
     */
    private String column;

    /**
     * 列的jdbc类型
     */
    private JdbcType jdbcType;


    /**
     * 属性名称
     */
    private String fieldName;

    /**
     * 属性
     */
    private Field field;

}
