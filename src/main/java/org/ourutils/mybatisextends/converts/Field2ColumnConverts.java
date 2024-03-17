package org.ourutils.mybatisextends.converts;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.ourutils.mybatisextends.bo.Field2ColumnBo;
import org.ourutils.mybatisextends.bo.UpdateField2ColumnBo;

import java.lang.reflect.Field;
import java.util.Map;

/**
 *******************************************************************************
 *    系统名称   ： mybatis-extends项目
 *    客户      ：  *
 *    文件名    ： org.ourutils.mybatisextends.converts.Field2ColumnConverts.java
 *              (C) Copyright 2023 our-utils Corporation
 *               All Rights Reserved.
 * *****************************************************************************
 * <PRE>
 * 作用
 *       属性与列信息转换类
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
@Mapper
public interface Field2ColumnConverts {

    Field2ColumnConverts INSTANCE = Mappers.getMapper(Field2ColumnConverts.class);

    /**
     * @author wsil
     * 
     * @param  fieldField2ColumnBoMap java对象的属性和其描述信息的映射集合
     * @return 修改函数的列及其描述信息的集合
     * <p>
     * 该方法的作用：
     *     用于转换列和属性对象信息
     * </p>
     */
    Map<Field, UpdateField2ColumnBo> cloneUpdateField2ColumnBo(Map<Field, Field2ColumnBo> fieldField2ColumnBoMap);

}
