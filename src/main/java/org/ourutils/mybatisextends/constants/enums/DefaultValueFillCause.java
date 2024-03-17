package org.ourutils.mybatisextends.constants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *******************************************************************************
 *    系统名称   ： mybatis-extends项目
 *    客户      ：  *
 *    文件名    ： org.ourutils.mybatisextends.constants.enums.DefaultValueFillCause.java
 *              (C) Copyright 2023 our-utils Corporation
 *               All Rights Reserved.
 * *****************************************************************************
 * <PRE>
 * 作用
 *       默认值填充原因
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
@Getter
@AllArgsConstructor
public enum DefaultValueFillCause {

    IFNULL("如果为null时触发", ""),
    IFEMPTY("如果为空时触发", ""),
    MyDefine("自定义触发条件,自定义触发条件传入的将是整个对象", "");

    /**
     * 中文释义
     */
    private String des;
    private String methodSign;
}
