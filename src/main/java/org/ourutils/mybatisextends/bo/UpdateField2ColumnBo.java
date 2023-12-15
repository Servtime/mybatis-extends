package org.ourutils.mybatisextends.bo;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.JdbcType;

import java.lang.reflect.Field;

/**
 *******************************************************************************
 *    系统名称   ： mybatis-extends项目
 *    客户      ：  *
 *    文件名    ： org.ourutils.mybatisextends.bo.UpdateField2ColumnBo.java
 *              (C) Copyright 2023 our-utils Corporation
 *               All Rights Reserved.
 * *****************************************************************************
 * <PRE>
 * 作用 修改列的时候封装的bo对象
 *
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
@Setter
@Getter
public class UpdateField2ColumnBo extends Field2ColumnBo {
    private boolean isIgnore = false;

    public UpdateField2ColumnBo(String column, JdbcType jdbcType, String fieldName, Field field) {
        super(column, jdbcType, fieldName, field);
    }
}
