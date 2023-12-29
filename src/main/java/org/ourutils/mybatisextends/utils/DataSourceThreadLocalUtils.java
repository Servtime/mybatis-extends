package org.ourutils.mybatisextends.utils;

import org.ourutils.mybatisextends.core.objs.LoggerWrap;
import org.ourutils.mybatisextends.core.objs.LoggerWrapFactory;

/**
 *******************************************************************************
 *    系统名称   ： mybatis-extends项目
 *    客户      ：  *
 *    文件名    ： org.ourutils.mybatisextends.utils.DataSourceThreadLocalUtils.java
 *              (C) Copyright 2023 our-utils Corporation
 *               All Rights Reserved.
 * *****************************************************************************
 * <PRE>
 * 作用
 *       多数据源获取当前正在使用的sql类型
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
public class DataSourceThreadLocalUtils {

    private static LoggerWrap log = LoggerWrapFactory.getLog(DataSourceThreadLocalUtils.class);

    private static ThreadLocal<String> threadLocal = new InheritableThreadLocal<>();


    /**
     * @author wsil
     * 
     * @param  key 需要设置的key
     * @param  v  需要设置的value
     * @return
     * <p>
     * 该方法的作用：
     *    设置线程上下文，设置新的值，返回旧的值
     * </p>
     */
    public static String setAndGet(String key, String v) {
        String old = threadLocal.get();
        threadLocal.set(v);
        return old;
    }

    /**
     * @author wsil
     * 
     * @param  key 获取对应的key
     * @return
     * <p>
     * 该方法的作用：
     *     获取线程上下文信息
     * </p>
     */
    public static String getValue(String key) {
        return threadLocal.get();
    }

}
