package org.ourutils.mybatisextends.constants.annotations;

import org.apache.ibatis.mapping.SqlCommandType;
import org.ourutils.mybatisextends.constants.enums.DefaultValueFillCause;
import org.ourutils.mybatisextends.core.objs.ColumnDefaultValueFill;
import org.ourutils.mybatisextends.core.objs.MybatisDefaultValue;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *******************************************************************************
 *    系统名称   ： mybatis-extends项目
 *    客户      ：  *
 *    文件名    ： org.ourutils.mybatisextends.constants.annotations.DefaultValueFill.java
 *              (C) Copyright 2023 our-utils Corporation
 *               All Rights Reserved.
 * *****************************************************************************
 * <PRE>
 * 作用
 *       默认值填充注解
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
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface DefaultValueFill {

    /**
     * 生效条件
     */
    DefaultValueFillCause defaultValueFillCause() default DefaultValueFillCause.IFNULL;

    /**
     * 当执行什么操作时生效
     */
    SqlCommandType[] happendTime();

    /**
     * 填充回调方法
     */
    Class<? extends MybatisDefaultValue> callBack() default ColumnDefaultValueFill.class;

    /**
     * 日期格式,仅转换成日期时有效
     */
    String dateFormate() default "yyyy-MM-dd HH:mm:ss";

    /**
     * 常量值
     */
    String constant() default "";

}
