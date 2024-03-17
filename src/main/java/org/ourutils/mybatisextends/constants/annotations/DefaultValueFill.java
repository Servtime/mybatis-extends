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
 *       默认值填充注解.该注解可以满足自增ID、创建日期、更新日期等需求
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
     * @author wsil
     * @return 判断触发条件
     * <p>       
     * 该方法的作用：
     *
     * </p>       
     */
    DefaultValueFillCause defaultValueFillCause() default DefaultValueFillCause.IFNULL;

    /**
     * @author wsil
     * @return 触发时机的枚举
     * <p>       
     * 该方法的作用：
     *   触发的时机
     * </p>       
     */
    SqlCommandType[] happendTime();

    /**
     * @author wsil
     *
     * @return 回调接口类
     * <p>
     * 该方法的作用:
     *
     * </p>
     */
    Class<? extends MybatisDefaultValue> callBack() default ColumnDefaultValueFill.class;

    /**
     * @author wsil
     *
     * @return 日期的格式
     * <p>
     * 该方法的作用: 由于日期具有多种格式，因此转换出来的日期对象，通过该方法指定
     *
     * </p>
     */
    String dateFormate() default "yyyy-MM-dd HH:mm:ss";

    /**
     * @author wsil
     *
     * @return 常量值
     * <p>
     * 该方法的作用:
     *  对于数据库里的有些列，我们可以直接指定默认值。
     * </p>
     */
    String constant() default "";

}
