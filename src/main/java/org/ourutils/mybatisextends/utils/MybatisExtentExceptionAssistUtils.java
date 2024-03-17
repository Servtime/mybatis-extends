package org.ourutils.mybatisextends.utils;

import org.ourutils.mybatisextends.constants.enums.ExceptionEnums;
import org.ourutils.mybatisextends.ex.MybatisExtentsException;

/**
 *******************************************************************************
 *    系统名称   ： mybatis-extends项目
 *    客户      ：  *
 *    文件名    ： org.ourutils.mybatisextends.utils.MybatisExtentExceptionAssistUtils.java
 *              (C) Copyright 2023 our-utils Corporation
 *               All Rights Reserved.
 * *****************************************************************************
 * <PRE>
 * 作用
 *     创建异常类信息的辅助类
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
public class MybatisExtentExceptionAssistUtils {


    /**
     * 用于快速创建一个异常类，
     *
     * @param exceptionEnums 异常枚举类
     * @param msges          异常的占位符信息
     * @return 异常对象
     */
    public static MybatisExtentsException newInstanceWhthThreadLocal(ExceptionEnums exceptionEnums, String... msges) {

        return new MybatisExtentsException(exceptionEnums.convertOrginMsg(msges));

    }


    /**
     * 包装异常类信息
     *
     * @param throwable 异常类
     * @param exceptionEnums 错误码枚举类
     * @param msges 错误码占位符
     * @return 包裹异常类的异常类
     */
    public static MybatisExtentsException wrapException(Throwable throwable, ExceptionEnums exceptionEnums, String... msges) {

        MybatisExtentsException mybatisExtentsException = newInstanceWhthThreadLocal(exceptionEnums, msges);

        mybatisExtentsException.initCause(throwable);

        return mybatisExtentsException;

    }
}

