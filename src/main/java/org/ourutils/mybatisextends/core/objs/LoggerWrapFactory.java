package org.ourutils.mybatisextends.core.objs;

import org.apache.ibatis.logging.LogFactory;

/**
 *******************************************************************************
 *    系统名称   ： mybatis-extends项目
 *    客户      ：  *
 *    文件名    ： org.ourutils.mybatisextends.core.LogFactoryWrap.java
 *              (C) Copyright 2023 our-utils Corporation
 *               All Rights Reserved.
 * *****************************************************************************
 * <PRE>
 * 作用
 *       工厂日志包装类
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
public class LoggerWrapFactory {

    /**
     * @author wsil
     * @date 2023/12/5
     * @param  clazz
     * @return
     * <p>
     * 该方法的作用：
     *    获取包装日志类
     * </p>
     */
    public static LoggerWrap getLog(Class<?> clazz) {
        return new LoggerWrap(LogFactory.getLog(clazz.getName()));
    }

    /**
     * @author wsil
     * @date 2023/12/5
     * @param  logger
     * @return
     * <p>
     * 该方法的作用：
     *  获取包装日志类
     * </p>
     */
    public static LoggerWrap getLog(String logger) {
        return new LoggerWrap(LogFactory.getLog(logger));
    }
}
