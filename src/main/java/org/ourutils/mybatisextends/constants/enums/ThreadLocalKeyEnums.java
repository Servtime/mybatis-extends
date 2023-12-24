package org.ourutils.mybatisextends.constants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *******************************************************************************
 *    系统名称   ： mybatis-extends项目
 *    客户      ：  *
 *    文件名    ： org.ourutils.mybatisextends.constants.enums.ThreadLocalKeyEnums.java
 *              (C) Copyright 2023 our-utils Corporation
 *               All Rights Reserved.
 * *****************************************************************************
 * <PRE>
 * 作用
 *       线程上下文枚举类
 * 限制
 *       无。
 * 注意事项
 *       无。
 * 修改历史
 * -----------------------------------------------------------------------------
 *         VERSION       DATE            @author       CHANGE/COMMENT
 * -----------------------------------------------------------------------------
 *         1.0.0        2023/12/24          wsil      create
 * -----------------------------------------------------------------------------
 * </PRE>
 **/
@AllArgsConstructor
@Getter
public enum ThreadLocalKeyEnums {

    DATASOURCE_PRODUCT("数据库类型");

    private String des;


}
