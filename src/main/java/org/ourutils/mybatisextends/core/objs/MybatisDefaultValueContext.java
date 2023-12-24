package org.ourutils.mybatisextends.core.objs;

import lombok.Data;
import org.apache.ibatis.mapping.SqlCommandType;
import org.ourutils.mybatisextends.constants.annotations.DefaultValueFill;

import java.util.Collection;

/**
 *******************************************************************************
 *    系统名称   ： mybatis-extends项目
 *    客户      ：  *
 *    文件名    ： org.ourutils.mybatisextends.core.objs.MybatisDefaultValueContext.java
 *              (C) Copyright 2023 our-utils Corporation
 *               All Rights Reserved.
 * *****************************************************************************
 * <PRE>
 * 作用
 *       mybatis填充默认属性的上下文磊
 * 限制
 *       无。
 * 注意事项
 *       无。
 * 修改历史
 * -----------------------------------------------------------------------------
 *         VERSION       DATE            @author       CHANGE/COMMENT
 * -----------------------------------------------------------------------------
 *         1.0.0        2023/12/21          wsil      create
 * -----------------------------------------------------------------------------
 * </PRE>
 **/
@Data
public class MybatisDefaultValueContext {

    /**
     * SQL的操作类型
     */
    private SqlCommandType sqlCommandType;

    /**
     * 同批次的其他对象
     */
    private Collection<Object> allObj;

    /**
     * 被填充的对象
     */
    private Object obj;

    /**
     * 注解信息
     */
    private DefaultValueFill defaultValueFill;
}
